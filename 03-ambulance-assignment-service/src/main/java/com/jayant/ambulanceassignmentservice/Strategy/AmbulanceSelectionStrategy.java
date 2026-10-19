package com.jayant.ambulanceassignmentservice.Strategy;

import com.jayant.ambulanceassignmentservice.Entity.Ambulance;
import com.jayant.ambulanceassignmentservice.Entity.Emergency;

import java.util.List;

public class AmbulanceSelectionStrategy {

    public Ambulance selectAmbulance(Emergency emergency , List<Ambulance> ambulanceList){
        if(emergency == null){
            throw new IllegalArgumentException("Emergency must not  be null");
        }
        if(emergency.getLatitude() == null || emergency.getLongitude() == null){
            throw new IllegalArgumentException(" Emergency Coordinator must not be null");
        }

        if(emergency.getLatitude() < -90 || emergency.getLatitude() > 90  || emergency.getLongitude() < -180 || emergency.getLongitude() >  180){
            throw new IllegalArgumentException("Invalid emergency coordinates");
        }
        if(ambulanceList == null ||  ambulanceList.isEmpty()){
            throw new IllegalArgumentException("Ambulance list must not be null or Empty ");
        }
        List<Ambulance> availableAmbulance = ambulanceList.stream()
                                            .filter(ambulance -> "AVAILABLE".equalsIgnoreCase(ambulance.getStatus()))
                                            .toList();

        List<Ambulance> suitableAmbulance = availableAmbulance.stream().filter(ambulance ->
                                                                                ambulance.getPatientCapacity() != null &&
                                                                                emergency.getPatientCount() != null &&
                                                                                ambulance.getPatientCapacity() >= emergency.getPatientCount()).toList();

        if(suitableAmbulance.isEmpty()){
            throw new IllegalStateException("No suitable ambulance available");
        }

        Ambulance nearestAmbulance = null;
        double minimumDistance = Double.MAX_VALUE;

        for(Ambulance ambulance : suitableAmbulance){

            if(ambulance.getLatitude() == null || ambulance.getLongitude() == null){
                continue;
            }
            if(ambulance.getLatitude() < -90 || ambulance.getLatitude() > 90 || ambulance.getLongitude() < -180 || ambulance.getLongitude() > 180){
                continue;
            }

            double distance = calculateDistance(emergency.getLatitude(), emergency.getLongitude(), ambulance.getLatitude(), ambulance.getLongitude());

            if(distance < minimumDistance){
                minimumDistance = distance;
                nearestAmbulance = ambulance;
            }
        }
        if(nearestAmbulance == null){
            throw new IllegalStateException("No ambulance with  valid coordinates available");
        }
        return nearestAmbulance;

    }
    public double calculateDistance(double emergencyLatitude , double emergencyLongitude , double ambulanceLatitude , double ambulanceLongitude){

        double emergencyLatitudeRadians = Math.toRadians(emergencyLatitude);
        double emergencyLongitudeRadians = Math.toRadians(emergencyLongitude);
        double ambulanceLatitudeRadians = Math.toRadians(ambulanceLatitude);
        double ambulanceLongitudeRadians = Math.toRadians(ambulanceLongitude);

        double latitudeDifference = ambulanceLatitudeRadians - emergencyLatitudeRadians;
        double longitudeDifference = ambulanceLongitudeRadians - emergencyLongitudeRadians;

        double latitudePart = Math.pow(Math.sin(latitudeDifference/2),2);
        double longitudePart = Math.pow(Math.sin(longitudeDifference/2),2);

        double a = latitudePart + Math.cos(emergencyLatitudeRadians) * Math.cos(ambulanceLatitudeRadians) * longitudePart;
        double safeA = Math.max(0.0, Math.min(1.0, a));
        double c = 2 * Math.atan2(Math.sqrt(safeA), Math.sqrt(1.0 - safeA));

        double distanceInKm = 6371 * c ;

        return distanceInKm;
    }
}
