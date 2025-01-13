const BasePage = require('./base.page');

class TeamCreationPage extends BasePage {
    constructor(page, flutterHelper) {
        super(page, flutterHelper);
        
        this.elements = {
            teamNameField: { x: 720, y: 300 },
            skillPositions: {
                position1: { x: 600, y: 400 },
                position2: { x: 600, y: 500 },
                position3: { x: 600, y: 600 }
            },
            addPlayerButton: { x: 720, y: 650 },
            createTeamButton: { x: 720, y: 800 },
            playerForm: {
                nameField: { x: 720, y: 400 },
                positionField: { x: 720, y: 500 },
                skillField: { x: 720, y: 600 },
                saveButton: { x: 720, y: 700 }
            }
        };

        this.text = {
            createTeam: 'Create Team',
            addPlayer: 'Add Player',
            required: 'required',
            success: 'success'
        };
    }

    async fillTeamName(teamName) {
        await this.flutterHelper.tap(
            this.elements.teamNameField.x,
            this.elements.teamNameField.y
        );
        await this.flutterHelper.typeText(teamName);
    }

    async selectSkills() {
        for (const position of Object.values(this.elements.skillPositions)) {
            await this.flutterHelper.tap(position.x, position.y);
            await this.waitForTimeout(500);
        }
    }

    async addPlayer(playerName) {
        // Click add player button
        await this.flutterHelper.tap(
            this.elements.addPlayerButton.x,
            this.elements.addPlayerButton.y
        );
        await this.waitForTimeout(1000);

        // Fill player details
        await this.flutterHelper.tap(
            this.elements.playerForm.nameField.x,
            this.elements.playerForm.nameField.y
        );
        await this.flutterHelper.typeText(playerName);

        // Select position
        await this.flutterHelper.tap(
            this.elements.playerForm.positionField.x,
            this.elements.playerForm.positionField.y
        );
        await this.waitForTimeout(500);

        // Select skill
        await this.flutterHelper.tap(
            this.elements.playerForm.skillField.x,
            this.elements.playerForm.skillField.y
        );
        await this.waitForTimeout(500);

        // Save player
        await this.flutterHelper.tap(
            this.elements.playerForm.saveButton.x,
            this.elements.playerForm.saveButton.y
        );
        await this.waitForTimeout(1000);
    }

    async createTeam() {
        await this.flutterHelper.tap(
            this.elements.createTeamButton.x,
            this.elements.createTeamButton.y
        );
        await this.waitForTimeout(2000);
    }

    async verifyTeamCreationPage() {
        const pageText = await this.getPageText();
        return pageText.includes(this.text.createTeam);
    }

    async verifyValidationMessage() {
        const pageText = await this.getPageText();
        return pageText.includes(this.text.required);
    }

    async verifySuccessMessage() {
        const pageText = await this.getPageText();
        return pageText.includes(this.text.success);
    }
}

module.exports = TeamCreationPage;
