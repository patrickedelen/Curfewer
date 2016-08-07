//start the mongodb connection and other configs

var async = require('async');
var mongoose = require('mongoose');

//internal requires
var fireBase = require('./app/FirebaseServer.js');
var LocationCheck = require('./app/LocationCheck.js');
var Users = require('./app/Users.js');

// start up mongodb
var db = mongoose.connect('mongodb://root:bu5tAnut@ds145315.mlab.com:45315/curfewer', function(err) {
//log database status
	if(err) {
		console.log(err);
	} else {
		console.log("MongoDB connected...");
	}
});

var data = {
    email: 'kid@gmail.com',
    token: '1234',
    latitude: 29.7362596,
    longitude: -95.7857027
};

var loginData = {
    email: 'gainesmitch2@gmail.com',
    token: '1234',
    role: 'Kid'
}

var parentData = {
    email: 'patrickedelen@gmail.com',
    kidEmail: 'gainesmitch2@gmail.com'
}
var d = new Date();
d.setMinutes(d.getMinutes() + 10);

var curfewData = {
    email: 'patrickedelen@gmail.com',
    kidEmail: 'kid@gmail.com',
    date: d
};

// Users.addChild(parentData, function(token) {
//     console.log(arguments);
// });

// Users.acceptParent(loginData, function(parentToken, kidEmail) {
//     console.log(arguments);
// });

// Users.addCurfew(curfewData, function(parentToken, kidToken, date) {
//     console.log(arguments);
// });

// LocationCheck.addLocation(data, function(kidToken, parentToken, curfewMinutes, minutesAway, childAddress, home) {
//     console.log(arguments);
// });
