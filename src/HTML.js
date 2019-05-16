const tools = require('./tools')

const legendColors = [
    ['file', 'cornflowerblue'],
    ['folder', 'darksalmon']
]

const formatHTML = (cdir, files) => {
    let list = '', legendStyles = '';

    legendColors.forEach(legend => {
        legendStyles += `.${legend[0]} {background-color: ${legend[1]};}`
    });

    files.forEach(file => {
        if(file.name[0] !== '.')
            list += `<div class="list_item ${(file.folder) ? 'folder' : 'file' }">
                    <div><p class="list-text"><b>${file.name}</b> (${tools.byte(file.size)})</p><p class="time_text">
                    Last Modified Time: ${tools.date(file.modified)}</p></div><a href="/${file.source}">
                    <button class="open_button">OPEN</button></a></div>`
    });

    let html = `<html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta http-equiv="X-UA-Compatible" content="ie=edge"><title>File Server</title><style>body {margin: 0;
                padding: 0;font-family:'Courier New', Courier, monospace}input {margin: 5px;margin-bottom: 10px;padding: 
                10px 20px;font-size: 15px;border-radius: 15px;border-style: none;border-color: #ebebeb;border-width: 1px;
                background-color: #ebebeb;align-self: flex-start;font-family:'Courier New', Courier, monospace
                }a {text-decoration: none;color: #000}p {margin: 
                0;}.main_container {display: flex;justify-content: center;align-items: center;}.inner_container {padding:
                10px;flex: .7;}.top_container {display: flex;justify-content: space-between;}.legend_container {display: 
                flex;flex-direction: row;justify-content: center;align-items: center;}.box-color {width: 20px;height:
                20px;background-color: lightgreen;margin: 0px 7px;border-radius: 10px;}.list_container {padding: 0px 10px;display: flex;justify-content: center;flex-direction: column;
                }.list_item {display: flex;padding: 10px;margin-bottom: 10px;border-radius: 5px;justify-content: space-between
                ;align-items: center;}.open_button {align-self: flex-end;font-size: 14px;padding: 6px 12px;border-radius: 15px;background-color: #fff;border-style: none;color: #000;}
                .time_text {font-size: 12px;}${legendStyles}@media only screen and (max-width: 500px) {input {align-self: center;width: 100%;}.list-text {font-size:
                13px;}.time_text {font-size: 11px;}.open_button {font-size: 12px;}.curr_dir {font-size: 15px;}}</style>
                </head><body><div class="main_container"><div class="inner_container"><div class="top_container"><a href="/">
                <h3 class="curr_dir">${tools.trim(cdir, 20)}</h3></a><div class="legend_container list-text"><div class="box-color 
                file"></div>File<div class="box-color folder"></div>Folder</div></div><div class="list_container">
                <input type="text" id="search" placeholder="Search"/>${list}</div>
                </div></div></body></html>`

    return html
}

module.exports = {
    format: formatHTML
}



