const path = require('path');
const fs = require('fs');
const url = require('url');

const folder = process.cwd();

module.exports = {
    extname: (filename) => {
        return (/[.]/.exec(filename)) ? /[^.]+$/.exec(filename)[0] : undefined;
    },

    getCurrentDirectoryBase: () => {
        return path.basename(process.cwd());
    },

    getDirname: () => {
        return path.dirname(process.cwd());
    },

    getLocation: (cdir, path) => {
        return path.replace(cdir, '')
    },

    getInfo: (requrl) => {
        let api = false;

        if(requrl.substring(0, 4) == '/api') {
            api = true;
            requrl = requrl.slice(4, requrl.length)

            if(requrl === '')
                requrl = '/'
        }

        // parse URL
        const parsedUrl = url.parse(requrl);
        // extract URL path
        let pathname = `.${parsedUrl.pathname.replace(/%20/gi, ' ')}` || '/';
        // based on the URL path, extract the file extention. e.g. .js, .doc, ...
        const ext = module.exports.extname(pathname);
        // maps file extention to MIME typere
        const map = {
            'ico': 'image/x-icon',
            'html': 'text/html',
            'js': 'text/javascript',
            'json': 'application/json',
            'css': 'text/css',
            'png': 'image/png',
            'jpg': 'image/jpeg',
            'jpeg': 'image/jpeg',
            'csv': 'text/csv',
            'css': 'text/css',
            'avi': 'video/x-msvideo',
            'wav': 'audio/wav',
            'mp3': 'audio/mpeg',
            'mpeg': 'video/mpeg',
            'svg': 'image/svg+xml',
            'pdf': 'application/pdf',
            'doc': 'application/msword',
            'zip': 'application/zip',
            'txt': 'text/plain',
        }

        return {
            url: requrl.replace(/%20/gi, ' '),
            pathname,
            ext,
            map,
            api
        }
    },

    getFiles: (cdir) => {
        let files = [];

        fs.readdirSync(cdir).forEach(file => {
            const stats = fs.statSync(`${cdir}/${file}`)

            let src_replace = cdir.replace(folder, '')
            let source = (src_replace == '/') ? '' : src_replace;
            source = `${source}/${file}`.substring(1, `${source}/${file}`.length);

            let data = {
                name: file,
                location: `${cdir}/${file}`,
                source,
                type: stats.isDirectory() ? 'folder' : module.exports.extname(file),
                size: stats.size,
                folder: stats.isDirectory(),
                access: stats.atime,
                modified: stats.mtime,
                created: stats.birthtime
            }

            files.push(data)
        });

        return files
    }
}