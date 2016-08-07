//modify and store all user settings

var async = require('async');
var mongoose = require('mongoose');

//require models
var modelKid = require('./models/kid.js');
var modelParent = require('./models/parent.js');

module.exports.LoginUser = function(data, callback) {

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

module.exports.UpdateHome = function(data, callback) {
    var parent = modelParent.find({"_email": data.email});

    parent.Home = {
        type: 'Point',
        coordinates: [data.longitude, data.latitude]
    };

    modelParent.update(
        {_email: data.email},
        parent
    );
    console.log('Updated home');
    callback(parent._token)
}

module.exports.AddCurfew = function(data, callback) {
    var parent = modelParent.find({"_email": data.email});

    parent.Curfews.push({
        kidEmail: data.kidEmail,
        date: data.date
    });

    modelParent.update(
        {_email: data.email},
        parent
    );
    console.log('Added new curfew');

    //update the kid
    var kid = modelKid.find({"_email": data.kidEmail});

    kid.Curfews.push({
        date: data.date
    });

    modelKid.update(
        {_email: data.kidEmail},
        kid
    );

    console.log('Updated kid');

    callback(parent._token, kid._token, data.date);
}

module.exports.addChild = function(data, callback) {
    //update the kid
    var kid = modelKid.find({"_email": data.kidEmail});

    kid._parentEmail = data.email;
    kid.accepted = false;

    modelKid.update(
        {_email: data.kidEmail},
        kid
    );

    console.log('Added unverified parent to kid');

    callback(kid._token, data.email);
}

module.exports.acceptParent = function(data, callback) {
    //update the kid
    var kid = modelKid.find({"_email": data.email});

    kid.accepted = true;

    modelKid.update(
        {_email: data.email},
        kid
    );
    console.log('Verified parent');

    //add the kid to the parent
    var parent = modelParent.find({"_email": data.email});

    parent.Kids.push({
        kidEmail: data.email
    });

    modelParent.update(
        {_email: data.email},
        parent
    );
    console.log('Added the kid to the parent');

    callback(parent._token, data.email);
}

module.exports.updateToken = function(data, callback) {
    if(data.role == "Kid") {
        var kid = modelKid.find({"_email": data.kidEmail});

        kid._token = data.token;

        modelKid.update(
            {_email: data.kidEmail},
            kid
        );

        console.log('Updated parent token');
    } else if (data.role == "Parent") {
        var parent = modelParent.find({"_email": data.email});

        parent._token = data.token;

        modelParent.update(
            {_email: data.email},
            parent
        );
        console.log('Updated parent token');

    }
    
    callback();
}
