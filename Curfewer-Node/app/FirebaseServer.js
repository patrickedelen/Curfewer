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
        case "AddCurfewFake":
        var kidToken = "dqDce37kV04:APA91bHnQgwbtlV2GNxJd94XtzD2aNCcmtD2m9iS02WRumWaBCqmekEmqD8B5699rNB1gW3QwgJZpb2SSbxY2xQ65Pv-1SIC_bQ2AgOh-rNd8umoTJuXEOYLJTVB8WiBSvwBF8IpE7kD";
        var notification = new Notification("NewCurfewAdded")
                        .title("New Curfew Added")
                        .body("Curfew added for Friday Night")
                        .build();

                    var messageKid = new Message("CurfewSet")
                        .priority("high")
                        .dryRun(false)
                        .addData("type", "CurfewSet")
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
        break;
        case "UserLoginMessage": //called from either flow, data {email: email, token: firebase token, role: parent or kid}
            console.log('User logged in');
            Users.loginUser(data, function(token, email) {
                var message = new Message("UserLoggedIn")
                    .priority("high")
                    .dryRun(false)
                    .addData("loggedIn", true)
                    .addData("email", email)
                    .addData("type", "UserLoggedIn")
                    .deliveryReceiptRequested(true)
                    .build();

                    console.log('sending');
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
                        .addData("type", "HomeSet")
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
            data.date = new Date();
            data.date.setMinutes(data.date.getMinutes() + 60);
                Users.addCurfew(data, function(token, kidToken, curfewDate) {
                    var message = new Message("CurfewSet")
                        .priority("high")
                        .dryRun(false)
                        .addData("curfewSet", true)
                        .addData("type", "CurfewSet")
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
                        .addData("type", "CurfewSet")
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
                        .addData("type", "ParentAccessRequest")
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
                        .addData("type", "ParentAccessGranted")
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
                            .addData("type", "ParentCurfewMessage")
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
                            .addData("type", "ChildCurfewMessage")
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
                                .addData("type", "ParentCurfewMessage")
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
                console.log('Getting all data');
                var message = new Message("AllDataReturn")
                        .priority("high")
                        .dryRun(false)
                        .addData("data", data)
                        .addData("type", "AllDataReturn")
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
            }
}); 
 
xcs.on('receipt', function(messageId, from, data, category) {
    console.log('received receipt', arguments);
});
 

var curfewMinutes = 2;
var minutesAway = 5;
var kidToken = 'dqDce37kV04:APA91bHnQgwbtlV2GNxJd94XtzD2aNCcmtD2m9iS02WRumWaBCqmekEmqD8B5699rNB1gW3QwgJZpb2SSbxY2xQ65Pv-1SIC_bQ2AgOh-rNd8umoTJuXEOYLJTVB8WiBSvwBF8IpE7kD';
var showKid = true;
if(showKid) {
var notificationKid = new Notification("ChildCurfewNotification")
        .title("Curfew is soon!")
        .body("Curfew is in " + curfewMinutes + " minutes and you are " + minutesAway + " minutes away")
        .build();

    var messageKid = new Message("ChildCurfewMessage")
        .priority("high")
        .dryRun(false)
        .addData("type", "ChildCurfewMessage")
        .deliveryReceiptRequested(true)
        .notification(notificationKid)
        .build();

    xcs.sendNoRetry(messageKid, kidToken, function (result) {
        if (result.getError()) {
            console.error(result.getErrorDescription());
        } else {
            console.log("message sent: #" + result.getMessageId());
        }
    });
}
var parent = true;
var childAddress = '1234 Main Street';
var parentToken = 'ePS8Dxq0ijk:APA91bF0-rmjfHKWy4quFWQrVy0_ACBIIq3eDCwKetVTqc1wOP3MCUXdS4UMDtjiJtj3p65x_YoywI9duVpvin4W8T2u3jA434XCwx0IIxCHCUqaVbUJcmpkeGq-hF8Ag1TqcqCX3d87';
if(parent) {
    var notificationParent = new Notification("ParentCurfewNotification")
                            .title("Your child missed their curfew")
                            .body("They are located at " + childAddress)
                            .build();

                            var messageParent = new Message("ParentCurfewMessage")
                                .priority("high")
                                .dryRun(false)
                                .addData("type", "ParentCurfewMessage")
                                .deliveryReceiptRequested(true)
                                .notification(notificationParent)
                                .build();

                            xcs.sendNoRetry(messageParent, parentToken, function (result) {
                                if (result.getError()) {
                                    console.error(result.getErrorDescription());
                                } else {
                                    console.log("message sent: #" + result.getMessageId());
                                }
                            });
}