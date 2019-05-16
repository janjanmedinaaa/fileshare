dateFormat = (date = new Date(), fstr = "%Y-%m-%d %H:%M:%S", utc = true) => {
    utc = utc ? 'getUTC' : 'get';
    return fstr.replace (/%[YmdHMS]/g, function (m) {
        switch (m) {
        case '%Y': return date[utc + 'FullYear'] (); // no leading zeros required
        case '%m': m = 1 + date[utc + 'Month'] (); break;
        case '%d': m = date[utc + 'Date'] (); break;
        case '%H': m = date[utc + 'Hours'] (); break;
        case '%M': m = date[utc + 'Minutes'] (); break;
        case '%S': m = date[utc + 'Seconds'] (); break;
        default: return m.slice (1); // unknown code, remove %
        }
        // add leading zero if required
        return ('0' + m).slice (-2);
    });
}

byteFormat = (bytes) => {
    if(bytes < 1000)
        return `${bytes}B`
    else if(bytes < 1000000)
        return `${bytes / 1000}KB`
    else if(bytes < 1000000000)
        return `${bytes / 1000000}MB`
    else 
        return `${bytes / 1000000000}GB`
}

trimString = (string, length = 30) => {
    if(string.length > length)
        return `...${string.slice(string.length-(length-3), string.length)}`

    return string
}

module.exports = {
    date: dateFormat,
    byte: byteFormat,
    trim: trimString
}