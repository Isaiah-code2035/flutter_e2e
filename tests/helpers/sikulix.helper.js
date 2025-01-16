const { _electron: electron } = require('playwright');
const path = require('path');
const { execSync } = require('child_process');

class SikulixHelper {
    constructor() {
        this.imagesPath = path.join(__dirname, '../images');
        this.sikulixJar = path.join(__dirname, '../../lib/sikulixide-2.0.5.jar');
    }

    async initialize() {
        // Ensure Sikulix jar exists
        if (!fs.existsSync(this.sikulixJar)) {
            console.log('Downloading Sikulix...');
            // Download Sikulix jar file
            const downloadUrl = 'https://launchpad.net/sikuli/sikulix/2.0.5/+download/sikulixide-2.0.5.jar';
            execSync(`curl -L ${downloadUrl} -o ${this.sikulixJar}`);
        }
    }

    async findAndClick(imageName, similarity = 0.7) {
        const imagePath = path.join(this.imagesPath, imageName);
        const script = `
            import org.sikuli.script.*;
            Screen s = new Screen();
            Pattern p = new Pattern("${imagePath}").similar(${similarity});
            s.wait(p, 10);
            s.click(p);
        `;
        
        return this.runSikulixScript(script);
    }

    async waitForImage(imageName, timeout = 10, similarity = 0.7) {
        const imagePath = path.join(this.imagesPath, imageName);
        const script = `
            import org.sikuli.script.*;
            Screen s = new Screen();
            Pattern p = new Pattern("${imagePath}").similar(${similarity});
            s.wait(p, ${timeout});
        `;
        
        return this.runSikulixScript(script);
    }

    async imageExists(imageName, similarity = 0.7) {
        const imagePath = path.join(this.imagesPath, imageName);
        const script = `
            import org.sikuli.script.*;
            Screen s = new Screen();
            Pattern p = new Pattern("${imagePath}").similar(${similarity});
            return s.exists(p) != null;
        `;
        
        return this.runSikulixScript(script);
    }

    async runSikulixScript(script) {
        const scriptPath = path.join(this.imagesPath, 'temp.sikuli', 'script.py');
        fs.writeFileSync(scriptPath, script);
        
        try {
            execSync(`java -jar ${this.sikulixJar} -r ${scriptPath}`);
            return true;
        } catch (error) {
            console.error('Sikulix script failed:', error);
            return false;
        }
    }
}

module.exports = SikulixHelper;
