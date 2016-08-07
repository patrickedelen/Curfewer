//require users module
var Users = require('./Users.js');
var LocationCheck = require('./LocationCheck.js');

var Sender = require('node-xcs').Sender;
var Message = require('node-xcs').Message;
var Notification = require('node-xcs').Notification;
var Result = require('node-xcs').Result;
 
var xcs = new Sender(45945011648, "AIzaSyC5817vsouhA6sCMhClMeCUETXA7jBpfic");
 
xcs.on('message', function(messageId, from, data, category) {
    console.log('Recieved message', arguments);

    switch(messageId) {
        case "UserLoginMessage": //called from either flow, data {email: email, token: firebase token, role: parent or kid}
            console.log('User logged in');
            Users.loginUser(data, function(token) {
                var message = new Message("UserLoggedIn")
                    .priority("high")
                    .dryRun(false)
                    .addData("loggedIn", true)
                    .deliveryReceiptRequested(true)
                    .build();

                xcs.sendNoRetry(message, token, function (result) {
                    if (result.getError()) {
                        console.error(result.getErrorDescription());
                    } else {
                        console.log("message sent: #" + result.getMessageId());
                    }
                });
            });
            break;
        case "UpdateHomeMessage": //called from the parent flow, data {email: parent email, token: parent token, latitude, longitude}
            console.log('User updated home');
                Users.updateHome(data, function(token) {
                    console.log(token);
                    var message = new Message("HomeSet")
                        .priority("high")
                        .dryRun(false)
                        .addData("HomeSet", true)
                        .deliveryReceiptRequested(true)
                        .build();

                    xcs.sendNoRetry(message, token, function (result) {
                        if (result.getError()) {
                            console.error(result.getErrorDescription());
                        } else {
                            console.log("message sent: #" + result.getMessageId());
                        }
                    });

                });
            break;
        case "AddCurfew": //called from the parent flow, data {email: parent email, token: parent token, kidEmail: kid email string, date: curfew date object}
            console.log('Added new curfew');
                Users.addCurfew(data, function(token, kidToken, curfewDate) {
                    var message = new Message("CurfewSet")
                        .priority("high")
                        .dryRun(false)
                        .addData("curfewSet", true)
                        .deliveryReceiptRequested(true)
                        .build();

                    xcs.sendNoRetry(message, token, function (result) {
                        if (result.getError()) {
                            console.error(result.getErrorDescription());
                        } else {
                            console.log("message sent: #" + result.getMessageId());
                        }
                    });

                    var notification = new Notification("NewCurfewAdded")
                        .title("New Curfew Added")
                        .body("Curfew added for ", curfewDate.getDate())
                        .build();

                    var messageKid = new Message("CurfewSet")
                        .priority("high")
                        .dryRun(false)
                        .addData("curfewDate", curfewDate)
                        .deliveryReceiptRequested(true)
                        .notification(notification)
                        .build();

                    xcs.sendNoRetry(messageKid, kidToken, function (result) {
                        if (result.getError()) {
                            console.error(result.getErrorDescription());
                        } else {
                            console.log("message sent: #" + result.getMessageId());
                        }
                    });


                });
            break;
        case "AddChildMessage": //called from the parent flow, data {email: parent's email, token: parent's token, kidEmail: kid's email}
            console.log('User added a child');
                Users.addChild(data, function(kidToken, parentEmail) {

                    var notification = new Notification("ParentRequestedAccess")
                        .title("A Parent requested access")
                        .body("Accept access from ", parentEmail)
                        .build();

                    var messageKid = new Message("ParentAccessRequest")
                        .priority("high")
                        .dryRun(false)
                        .addData("parentEmail", parentEmail)
                        .deliveryReceiptRequested(true)
                        .notification(notification)
                        .build();

                    xcs.sendNoRetry(messageKid, kidToken, function (result) {
                        if (result.getError()) {
                            console.error(result.getErrorDescription());
                        } else {
                            console.log("message sent: #" + result.getMessageId());
                        }
                    });


                });
            break;
            case "AcceptParent": //called from the child flow, data {email: kid's email, token: kid's token}
            console.log('User accepted parent request');
                Users.acceptParent(data, function(parentToken, kidEmail) {

                    var notification = new Notification("ParentRequestGranted")
                        .title("Your child accepted access")
                        .body("New child ", kidEmail)
                        .build();

                    var messageParent = new Message("ParentAccessGranted")
                        .priority("high")
                        .dryRun(false)
                        .addData("acceptedRequest", true)
                        .deliveryReceiptRequested(true)
                        .notification(notification)
                        .build();

                    xcs.sendNoRetry(messageParent, parentToken, function (result) {
                        if (result.getError()) {
                            console.error(result.getErrorDescription());
                        } else {
                            console.log("message sent: #" + result.getMessageId());
                        }
                    });


                });
            break;

            case "AddLocation": //called from the child flow, data {email: kid's email, token: kid's token, latitude: kid lat, longitude: kid lon}
            console.log('New location has been reported');
                LocationCheck.addLocation(data, function(kidToken, parentToken, curfewMinutes, minutesAway, childAddress, home) {

                    if(home) {
                        console.log('Made it home');

                        var notificationParent = new Notification("ParentCurfewNotification")
                        .title("Your child made it home!")
                        .body("Their curfew was in " + curfewMinutes + " minutes")
                        .build();

                        var messageParent = new Message("ParentCurfewMessage")
                            .priority("high")
                            .dryRun(false)
                            .deliveryReceiptRequested(true)
                            .notification(notification)
                            .build();

                        xcs.sendNoRetry(messageParent, parentToken, function (result) {
                            if (result.getError()) {
                                console.error(result.getErrorDescription());
                            } else {
                                console.log("message sent: #" + result.getMessageId());
                            }
                        });
                    } else {

                        var notificationKid = new Notification("ChildCurfewNotification")
                            .title("Curfew is soon!")
                            .body("Curfew is in " + curfewMinutes + " minutes and you are " + minutesAway + " minutes away")
                            .build();

                        var messageKid = new Message("ChildCurfewMessage")
                            .priority("high")
                            .dryRun(false)
                            .deliveryReceiptRequested(true)
                            .notification(notification)
                            .build();

                        xcs.sendNoRetry(messageKid, kidToken, function (result) {
                            if (result.getError()) {
                                console.error(result.getErrorDescription());
                            } else {
                                console.log("message sent: #" + result.getMessageId());
                            }
                        });

                        if(curfewMinutes <= 0) {
                            var notificationParent = new Notification("ParentCurfewNotification")
                            .title("Your child missed their curfew")
                            .body("They are located at " + childAddress)
                            .build();

                            var messageParent = new Message("ParentCurfewMessage")
                                .priority("high")
                                .dryRun(false)
                                .deliveryReceiptRequested(true)
                                .notification(notification)
                                .build();

                            xcs.sendNoRetry(messageParent, parentToken, function (result) {
                                if (result.getError()) {
                                    console.error(result.getErrorDescription());
                                } else {
                                    console.log("message sent: #" + result.getMessageId());
                                }
                            });
                        }

                    }
                });
            break;

            case "UpdateToken": //sent from either parent or child data {email: email, token: new token, role: Parent or Kid}
            Users.updateToken(data, function(token) {
                console.log('Token updated');
            });
            break;
            case "GetAll": //sent from either parent or child data {email: email, token: token, role: Parent or Kid}
            Users.getAll(data, function(token, data) {
                console.log(arguments);
                var message = new Message("AllDataReturn")
                        .priority("high")
                        .dryRun(false)
                        .addData("data", data)
                        .deliveryReceiptRequested(true)
                        .notification(notification)
                        .build();

                    xcs.sendNoRetry(message, token, function (result) {
                        if (result.getError()) {
                            console.error(result.getErrorDescription());
                        } else {
                            console.log("message sent: #" + result.getMessageId());
                        }
                    });
            });
            break;
            }
}); 
 
xcs.on('receipt', function(messageId, from, data, category) {
    console.log('received receipt', arguments);
});
 