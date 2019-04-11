const {dialog} = require('electron').remote

function getDirectory(){
    dialog.showOpenDialog({ properties: ['openFile', 'openDirectory', 'multiSelections'] });
}