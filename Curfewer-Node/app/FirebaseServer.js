var Sender = require('node-xcs').Sender;
var Message = require('node-xcs').Message;
var Notification = require('node-xcs').Notification;
var Result = require('node-xcs').Result;
 
var xcs = new Sender(45945011648, "AIzaSyC5817vsouhA6sCMhClMeCUETXA7jBpfic");
 
xcs.on('message', function(messageId, from, data, category) {
    console.log('received message', arguments);
}); 
 
xcs.on('receipt', function(messageId, from, data, category) {
    console.log('received receipt', arguments);
});
 
var notification = new Notification("ic_launcher")
    .title("Hello buddy!")
    .body("node-xcs is awesome!!!")
    .build();
 
var message = new Message("Data")
    .priority("high")
    .dryRun(false)
    .addData("node-xcs", true)
    .addData("anything_else", false)
    .addData("awesomeness", 100)
    .deliveryReceiptRequested(true)
    .notification(notification)
    .build();

var clientToken = "dY9lbYylYFo:APA91bFEcAJi0F-tHajysdVn42Gajf8tpNifXS71xq3qvmnmQBnLqyIRx1qYoWFny9RBZcvJoPd8WV4RrpVbgpsRCJhuKnBPrP-8m63IWn895auXGJLheypMxxjBZWFM38DrxFR-1ZpO";

xcs.sendNoRetry(message, clientToken, function (result) {
    if (result.getError()) {
        console.error(result.getErrorDescription());
    } else {
        console.log("message sent: #" + result.getMessageId());
    }
});