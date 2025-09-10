const fs = require('fs');
const path = require('path');

// 递归查找所有 .less 和 .vue 文件
function findFiles(dir, extensions = ['.less', '.vue']) {
    let results = [];
    const list = fs.readdirSync(dir);
    
    list.forEach(file => {
        const filePath = path.join(dir, file);
        const stat = fs.statSync(filePath);
        
        if (stat && stat.isDirectory()) {
            results = results.concat(findFiles(filePath, extensions));
        } else {
            const ext = path.extname(file);
            if (extensions.includes(ext)) {
                results.push(filePath);
            }
        }
    });
    
    return results;
}

// 修复文件中的引用路径
function fixImports(filePath) {
    let content = fs.readFileSync(filePath, 'utf8');
    let modified = false;
    
    // 替换 @fb/admin-base/src/assets/styles/ 为相对路径
    const oldPattern = /@import\s+~?["']@fb\/admin-base\/src\/assets\/styles\/([^"']+)["'];?/g;
    
    content = content.replace(oldPattern, (match, stylePath) => {
        modified = true;
        
        // 计算相对路径
        const relativePath = path.relative(path.dirname(filePath), path.join(__dirname, 'src', 'admin-base', 'assets', 'styles'));
        const normalizedPath = relativePath.replace(/\\/g, '/');
        
        console.log(`修复文件: ${filePath}`);
        console.log(`  原路径: @fb/admin-base/src/assets/styles/${stylePath}`);
        console.log(`  新路径: ${normalizedPath}/${stylePath}`);
        
        return `@import "${normalizedPath}/${stylePath}";`;
    });
    
    if (modified) {
        fs.writeFileSync(filePath, content, 'utf8');
        return true;
    }
    
    return false;
}

// 主函数
function main() {
    const adminBaseDir = path.join(__dirname, 'src', 'admin-base');
    
    if (!fs.existsSync(adminBaseDir)) {
        console.error('admin-base 目录不存在');
        return;
    }
    
    const files = findFiles(adminBaseDir);
    let fixedCount = 0;
    
    files.forEach(file => {
        if (fixImports(file)) {
            fixedCount++;
        }
    });
    
    console.log(`\n修复完成，共处理 ${fixedCount} 个文件`);
}

main();