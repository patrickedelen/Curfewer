//check location and send back notifications

var async = require('async');
var mongoose = require('mongoose');

//google maps api
var GoogleMapsAPI = require('googlemaps');
var mapsConfig = {
	key: 'AIzaSyBMDV4_xucQMtPhCWmxG5fa5OGpjgi07Ik',
	stagger_time:       1000, // for elevationPath
  	encode_polylines:   false,
  	secure:             true // use https
}
//create google maps api object
var gmAPI = new GoogleMapsAPI(mapsConfig);

//require models
var modelKid = require('./models/kid.js');
var modelParent = require('./models/parent.js');

module.exports.addLocation = function(data, callback) { //called from the child flow, data {email: kid's email, token: kid's token, latitude: kid lat, longitude: kid lon}
    var curDate = new Date();

    modelKid.findOne({"_email": data.email}).exec(function(err, docKid) {
        var curfewDate = docKid.Curfews[0].date;
        var curfewMinutes = curfewDate.getUTCMinutes() - curDate.getUTCMinutes();

        modelParent.findOne({"_email": docKid._parentEmail}).exec(function(err, docParent) {
            var params = {
                origin: data.latitude + ',' + data.longitude,
                destination: docParent.Home.coordinates[1] + ',' + docParent.Home.coordinates[0]
            };

            gmAPI.directions(params, function(err, results) {
				if(!err) {
                    var minutesAway = results.routes[0].legs[0].duration.value / 60;
					var childAddress = results.routes[0].legs[0].start_address;

                    if(results.routes[0].legs[0].duration.value < 100) {
                        var home = true;
                        docKid.Curfews.pop();
                        docKit.save();
                    } else {
                        var home = false;
                    }

                        callback(docKid._token, docParent._token, curfewMinutes, minutesAway, childAddress, home);

				} else {
					console.log(err);
				}

                //call the callback

			});
        });

        
        

    });
}//returning kidToken, parentToken, curfewMinutes, minutesAway, childAddress, home