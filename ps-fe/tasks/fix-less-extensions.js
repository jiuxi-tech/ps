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

// 修复文件中的 less 引用后缀名
function fixLessExtensions(filePath) {
    let content = fs.readFileSync(filePath, 'utf8');
    let modified = false;
    
    // 匹配没有 .less 后缀的 @import 语句
    // 排除已经有扩展名的引用（如 .less, .css 等）
    const pattern = /@import\s+["']([^"']*(?<!\.[a-zA-Z]{2,4}))["'];?/g;
    
    content = content.replace(pattern, (match, importPath) => {
        // 跳过已经有扩展名的文件
        if (/\.[a-zA-Z]{2,4}$/.test(importPath)) {
            return match;
        }
        
        // 跳过以 ~ 开头的特殊引用
        if (importPath.startsWith('~')) {
            return match;
        }
        
        modified = true;
        console.log(`修复文件: ${filePath}`);
        console.log(`  原引用: ${importPath}`);
        console.log(`  新引用: ${importPath}.less`);
        
        return `@import "${importPath}.less";`;
    });
    
    if (modified) {
        fs.writeFileSync(filePath, content, 'utf8');
        return true;
    }
    
    return false;
}

// 主函数
function main() {
    const srcDir = path.join(__dirname, 'src');
    
    if (!fs.existsSync(srcDir)) {
        console.error('src 目录不存在');
        return;
    }
    
    const files = findFiles(srcDir);
    let fixedCount = 0;
    
    files.forEach(file => {
        if (fixLessExtensions(file)) {
            fixedCount++;
        }
    });
    
    console.log(`\n修复完成，共处理 ${fixedCount} 个文件`);
}

main();