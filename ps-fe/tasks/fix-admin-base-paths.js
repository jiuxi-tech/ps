const fs = require('fs');
const path = require('path');

// Function to recursively find all .vue and .js files
function findFiles(dir, fileList = []) {
    const files = fs.readdirSync(dir);
    
    files.forEach(file => {
        const filePath = path.join(dir, file);
        const stat = fs.statSync(filePath);
        
        if (stat.isDirectory()) {
            findFiles(filePath, fileList);
        } else if (file.endsWith('.vue') || file.endsWith('.js')) {
            fileList.push(filePath);
        }
    });
    
    return fileList;
}

// Function to fix import paths in a file
function fixImportPaths(filePath) {
    try {
        let content = fs.readFileSync(filePath, 'utf8');
        let modified = false;
        
        // Replace '../../../../../' with '../../../../' in import statements and @import
        const patterns = [
            /from ['"]\.\.\/.\.\/\.\.\/.\.\/\.\.\/.\.\/([^'"]+)['"]/g,
            /import\(['"]\.\.\/.\.\/\.\.\/.\.\/\.\.\/.\.\/([^'"]+)['"]\)/g,
            /@import ['"]\.\.\/.\.\/\.\.\/.\.\/\.\.\/.\.\/([^'"]+)['"]/g
        ];
        
        let newContent = content;
        
        patterns.forEach(pattern => {
            newContent = newContent.replace(pattern, (match, path) => {
                return match.replace('../../../../../', '../../../../');
            });
        });
        
        if (newContent !== content) {
            fs.writeFileSync(filePath, newContent, 'utf8');
            console.log(`Fixed paths in: ${filePath}`);
            modified = true;
        }
        
        return modified;
    } catch (error) {
        console.error(`Error processing ${filePath}:`, error.message);
        return false;
    }
}

// Main execution
const adminBaseDir = path.join(__dirname, 'admin-base');
const files = findFiles(adminBaseDir);

console.log(`Found ${files.length} files to check...`);

let fixedCount = 0;
files.forEach(file => {
    if (fixImportPaths(file)) {
        fixedCount++;
    }
});

console.log(`\nFixed import paths in ${fixedCount} files.`);
console.log('Path fixing completed!');