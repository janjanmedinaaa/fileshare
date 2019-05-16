#!/usr/bin/env node 

const http = require('http');
const fs = require('fs');
const ngrok = require('ngrok');
const os = require( 'os' );
const argv = require('yargs').argv;
const qrcode = require('qrcode-terminal');

const files = require('./src/files');
const html = require('./src/HTML');

const folder = process.cwd();
const networkInterfaces = os.networkInterfaces();

// const authtoken = '7ho3YEqUP3yeVMzewhZqW_5FFdaRD2wvbJsvUoKv9Jc';

var port = argv.port || 4576

startNGROK = async(authtoken) => {
    console.log('Connecting to NGROK...');

    try{
        const url = await ngrok.connect({
            port, // port or network address, defaults to 80
            authtoken, // your authtoken from ngrok.com
        });

        console.log(`ngrok URL: \x1b[32m${url}\x1b[0m`);

        console.log('\nScan QR Code using the FileShare App:')
        qrcode.generate(`${url}/api`);
    } catch(e) {
        console.log(e)
    }
}

if(argv.ngrok !== undefined && argv.ngrok != '' && argv.ngrok != null)
    startNGROK(argv.ngrok);

http.createServer(function (req, res) {
    let { url, pathname, ext, map, api } = files.getInfo(req.url)

    console.log(`${req.method} ${url} ${api}`)

    fs.exists(pathname, function (exist) {
        if(!exist) {
            // if the file is not found, return 404
            res.statusCode = 404;
            res.end(`File ${pathname} not found!`);
            return;
        }

        // if is a directory search for index file matching the extention
        if (fs.statSync(pathname).isDirectory()) pathname += '/index' + ext;

        // read file from file system
        fs.readFile(pathname, function(err, data){
            if(err){ 
                let getFiles = `${folder}${url}`;

                if(api) {
                    res.setHeader('Content-Type', 'application/json');
                    res.end(JSON.stringify(files.getFiles(getFiles), null, 2))
                } else {
                    res.setHeader('Content-Type', 'text/html');
                    res.end(html.format(getFiles, files.getFiles(getFiles)))
                }

            } else {
                // if the file is found, set Content-type and send data
                res.setHeader('Content-Type', map[ext] || 'text/plain' );
                res.end(data);
            }
        });
    });


}).listen(parseInt(port));

console.log(`localhost URL: \x1b[32mlocalhost:${port}\x1b[0m`);
console.log(`lan URL: \x1b[32m${networkInterfaces.en0[1].address}:${port}\x1b[0m`);

if(argv.ngrok === undefined || argv.ngrok == '' || argv.ngrok === null) {
    console.log('\nScan QR Code using the FileShare App:')
    qrcode.generate(`http://${networkInterfaces.en0[1].address}:${port}/api`);
}