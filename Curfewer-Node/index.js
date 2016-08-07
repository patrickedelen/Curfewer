//start the mongodb connection and other configs

var async = require('async');
var mongoose = require('mongoose');

//internal requires
var fireBase = require('./app/FirebaseServer.js');

// start up mongodb
var db = mongoose.connect('mongodb://root:bu5tAnut@ds145315.mlab.com:45315/curfewer', function(err) {
//log database status
	if(err) {
		console.log(err);
	} else {
		console.log("MongoDB connected...");
	}
});