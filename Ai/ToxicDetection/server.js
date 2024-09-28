const http = require('http');
const request = require('request');

_EXTERNAL_URL = 'http://127.0.0.1:5000/detect'
_POST_URL = 'http://127.0.0.1:5000/submit'

const func = "post"

const callExternalApi = (callback) => {
    request(_EXTERNAL_URL, { json: true}, (error, response, body) => {
        if (!error && response.statusCode == 200) {
            callback(body); 
        } else {
            callback({ error: 'Error calling external API' });
        }
    });
}

const postToExternalApi = (postData, callback) => {
    request.post({
        url: _POST_URL,
        json: postData
    }, (error, response, body) => {
        if (!error && response.statusCode == 200) {
            callback(body); 
        } else {
            callback({ error: 'Error posting data to external API' });
        }
    });
}

http.createServer((req,res) =>{
    if (func === 'get'){
        callExternalApi(function(response){
            res.writeHead(200, { 'Content-Type': 'application/json' });
            res.write(JSON.stringify(response));
            res.end();
        });
    }
    if (func === 'post'){
        const dataToSend = {
            word: "fat"      //Provide your data here
        };
        postToExternalApi(dataToSend, function(response) {
            res.writeHead(200, { 'Content-Type': 'application/json' });
            res.write(JSON.stringify(response)); 
            res.end();
        });
    }
}).listen(3000);


console.log("Service running on 3000 port....")