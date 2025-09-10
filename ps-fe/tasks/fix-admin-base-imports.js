const fs = require('fs');
const path = require('path');
const glob = require('glob');

// 递归查找所有需要处理的文件
function findFiles(dir, extensions = ['.js', '.vue']) {
  const files = [];
  
  function traverse(currentDir) {
    const items = fs.readdirSync(currentDir);
    
    for (const item of items) {
      const fullPath = path.join(currentDir, item);
      const stat = fs.statSync(fullPath);
      
      if (stat.isDirectory()) {
        traverse(fullPath);
      } else if (extensions.some(ext => item.endsWith(ext))) {
        files.push(fullPath);
      }
    }
  }
  
  traverse(dir);
  return files;
}

// 替换文件中的引用
function replaceImports(filePath) {
  let content = fs.readFileSync(filePath, 'utf8');
  let changed = false;
  
  // 计算相对路径
  const relativePath = path.relative(path.dirname(filePath), path.join(__dirname, 'src', 'admin-base'));
  const normalizedPath = relativePath.replace(/\\/g, '/');
  
  // 替换各种引用模式
  const patterns = [
    // import语句
    /@fb\/admin-base\/src\//g,
    // require语句
    /@fb\/admin-base\/src\//g,
    // 动态import
    /@fb\/admin-base\/src\//g,
    // CSS import
    /~"@fb\/admin-base\/src\//g
  ];
  
  patterns.forEach(pattern => {
    if (pattern.test(content)) {
      if (pattern.source.includes('~"')) {
        // CSS import处理
        content = content.replace(pattern, `~"${normalizedPath}/`);
      } else {
        // JS import处理
        content = content.replace(pattern, `${normalizedPath}/`);
      }
      changed = true;
    }
  });
  
  // 特殊处理@fb/admin-base主包引用
  const mainPackagePattern = /from ['"]@fb\/admin-base['"]/g;
  if (mainPackagePattern.test(content)) {
    content = content.replace(mainPackagePattern, `from '${normalizedPath}'`);
    changed = true;
  }
  
  const importMainPattern = /import\(['"]@fb\/admin-base['"]/g;
  if (importMainPattern.test(content)) {
    content = content.replace(importMainPattern, `import('${normalizedPath}'`);
    changed = true;
  }
  
  if (changed) {
    fs.writeFileSync(filePath, content, 'utf8');
    console.log(`Updated: ${filePath}`);
  }
}

// 主执行函数
function main() {
  const adminBaseDir = path.join(__dirname, 'src', 'admin-base');
  
  if (!fs.existsSync(adminBaseDir)) {
    console.error('admin-base directory not found!');
    return;
  }
  
  console.log('Finding files to update...');
  const files = findFiles(adminBaseDir);
  
  console.log(`Found ${files.length} files to process`);
  
  files.forEach(file => {
    try {
      replaceImports(file);
    } catch (error) {
      console.error(`Error processing ${file}:`, error.message);
    }
  });
  
  console.log('Import replacement completed!');
}

// 如果直接运行此脚本
if (require.main === module) {
  main();
}

module.exports = { main, replaceImports, findFiles };