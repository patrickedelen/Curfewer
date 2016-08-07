//modify and store all user settings

var async = require('async');
var mongoose = require('mongoose');

//require models
var modelKid = require('./models/kid.js');
var modelParent = require('./models/parent.js');

module.exports.loginUser = function(data, callback) {

    if(data.role == 'Kid') {
        console.log('Kid login flow reached');
        var kidDocument = {
            _email: data.email,
            _token: data.token
        };

        modelKid.where('_email', data.email, function(userReturned) {
            console.log('searching db');
				
			}).exec(function(err, userReturned) {
                if(userReturned.length == 0) {
                    console.log('Adding user');
					try {
						modelKid.collection.insert(kidDocument, function(err) {
                            if(err){
                                console.log(err);
                            } else {
                                callback(kidDocument._token);
                            }
                        });
					} catch (e) {
						print (e);
					};
				} else {
					console.log('User already added');
                    callback(kidDocument._token);
				}
                callback(kidDocument._token);
		    });

    } else if(data.role == 'Parent') {
        console.log('Parent login flow reached');
        var parentDocument = {
            _email: data.email,
            _token: data.token
        };

        modelParent.where('_email', data.email, function(userReturned) {
            console.log('searching db');
				
			}).exec(function(err, userReturned) {
                if(userReturned.length == 0) {
                    console.log('Adding user');
					try {
						modelParent.collection.insert(parentDocument, function(err) {
                            if(err){
                                console.log(err);
                            } else {
                                callback(parentDocument._token);
                            }
                        });
					} catch (e) {
						print (e);
					};
				} else {
					console.log('User already added');
                    callback(parentDocument._token);
				}
		    });
    }
}

module.exports.updateHome = function(data, callback) {
    modelParent.findOne({"_email": data.email}).exec(function(err, doc) { 

        doc.Home = {
            type: 'Point',
            coordinates: [data.longitude, data.latitude]
        };

        doc.save();
        console.log('Updated home');
        callback(doc._token);

    });

    
}

module.exports.addCurfew = function(data, callback) {
    modelParent.findOne({"_email": data.email}).exec(function(err, docParent) { 

    docParent.Curfews.push({
        kidEmail: data.kidEmail,
        date: data.date
    });

    docParent.save();
    console.log('Added new curfew');

    //update the kid
    modelKid.findOne({"_email": data.kidEmail}).exec(function(err, docKid) {
        
        docKid.Curfews.push({
            date: data.date
        });


        docKid.save();
        console.log('Updated kid');

        callback(docParent._token, docKid._token, data.date);

        });

    });
}

module.exports.addChild = function(data, callback) {
    //update the kid
    modelKid.findOne({"_email": data.kidEmail}).exec(function(err, docKid) {
        docKid._parentEmail = data.email;
        docKid.accepted = false;

        docKid.save();

        console.log('Added unverified parent to kid');

        callback(docKid._token, data.email);
    });
}

module.exports.acceptParent = function(data, callback) {
    //update the kid
    modelKid.findOne({"_email": data.email}).exec(function(err, docKid) { 

        docKid.accepted = true;
        docKid.save();

        console.log('Verified parent');

        modelParent.findOne({"_email": docKid._parentEmail}).exec(function(err, docParent) {

            docParent.Kids.push({
                kidEmail: data.email
            });

            docParent.save();
            console.log('Added the kid to the parent');

            callback(docParent._token, data.email);
        });

    });
}

module.exports.updateToken = function(data, callback) {
    if(data.role == "Kid") {
        modelKid.findOne({"_email": data.email}).exec(function(err, docKid) {
        
            docKid._token = data.token;

            docKid.save();

            console.log('Updated kid token');

            callback();
        });
    } else if (data.role == "Parent") {
        modelParent.findOne({"_email": data.email}).exec(function(err, docParent) {
            
            docParent._token = data.token;

            docParent.save();

            console.log('Updated parent token');

            callback();
        });

    }
}

module.exports.getAll = function(data, callback) {
     if(data.role == "Kid") {
        modelKid.findOne({"_email": data.email}).exec(function(err, docKid) {
            callback(docKit._token, docKid);
        });
    } else if (data.role == "Parent") {
        modelParent.findOne({"_email": data.email}).exec(function(err, docParent) {
            callback(docParent._token, docParent);
        });

    }
}
