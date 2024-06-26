package se.kth.ii1305.gulsparv.sproutview;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringJoiner;




public class PlantCalculator {

    private static PlantCalculator INSTANCE = new PlantCalculator();

    private PlantCalculator() {
    }

    public static PlantCalculator getInstance() {

        return INSTANCE;

    }

    boolean plantCare = false;
    int count = 0;

    int indexValueOfMatchingElements = -1;

    public JSONObject calculateTimeline(JSONObject options, JSONObject queryResult) {

        // beräkna timeline, vi får ett objekt med options och query resultet

        // to calculate the timeline depending on the options that I have I need to
        // query it an get the result. But when I have

        System.out.println(options.getString(true));
        System.out.println("\n");
        System.out.println(queryResult.getString(true));

        String optionsLight = options.getValue("light_level")[0];
        String[] resultLight = queryResult.getValue("light_level");
        double lightSimilarity = 0;
        for (int n = 0; n < resultLight.length; n++) {
            double currentLightSimilarity = lightCompare(optionsLight, resultLight[n]);
            if (currentLightSimilarity > lightSimilarity) {
                lightSimilarity = currentLightSimilarity;
            }
        }

    

        StringJoiner joiner = new StringJoiner(" & ");
        for(int i = 0; i < resultLight.length;i++){
            joiner.add(resultLight[i]);
        }
        String allLights = joiner.toString();

       

        String optionsSoil = options.getValue("soil")[0];
        String[] resultSoil = queryResult.getValue("soil");
        double soilSimilarity = 0;
        for (int n = 0; n < resultSoil.length; n++) {
            double currentSoilSimilarity = soilCompare(optionsSoil, resultSoil[n]);
            if (currentSoilSimilarity > soilSimilarity) {
                soilSimilarity = currentSoilSimilarity;
            }
        }

        String optionsWater = options.getValue("preferred_watering_frequency")[0];
        String[] resultWater = queryResult.getValue("preferred_watering_frequency");
        double waterSimilarity = 0;
        for (int n = 0; n < resultWater.length; n++) {
            double currentWaterSimilarity = waterCompare(optionsWater, resultWater[n]);
            if (currentWaterSimilarity > waterSimilarity) {
                waterSimilarity = currentWaterSimilarity;
            }
        }

        String optionsTemp = options.getValue("preferred_average_temperature")[0];
        String[] resultTemp = queryResult.getValue("preferred_average_temperature");
        double tempSimilarity = 0;
        for (int n = 0; n < resultTemp.length; n++) {
            double currentTempSimilarity = temperatureCompare(optionsTemp, resultTemp[n]);
            if (currentTempSimilarity > tempSimilarity) {
                tempSimilarity = currentTempSimilarity;
            }
        }

        String optionsPotsize = options.getValue("preferred_pot_size")[0];
        String resultPotsize = queryResult.getValue("preferred_pot_size")[0];
        double potSizeSimilarity = potSizeCompare(optionsPotsize, resultPotsize);

        String[] optionsPlantCare = options.getValue("plant_care");
        String[] resultPlantCare = queryResult.getValue("plant_care");
        //double plantCareSimilarity = 1.0;
        
    
        for (int i = 0; i < optionsPlantCare.length; i++){
            if (optionsPlantCare[i].equals(" ")) {
                count++;
 
            }
        }

        double plantCareSimilarity = plantCareCompareSeveralNew(optionsPlantCare, resultPlantCare);

        StringJoiner plantCareJoiner = new StringJoiner(" & ");
        for(int k = 0; k < optionsPlantCare.length; k++){
            if(indexValueOfMatchingElements > 0){
                if(k != indexValueOfMatchingElements){
                    plantCareJoiner.add(optionsPlantCare[k]);
                }
            }
        }

        String allPlantCare = plantCareJoiner.toString();

        String optionsHumidity = options.getValue("humidity")[0];
        String resultHumidity = queryResult.getValue("humidity")[0].replace("%", "");
        double humiditySimilarity = 1.0;
        boolean humidity = false;
        if (!optionsHumidity.equals("")) {
            humiditySimilarity = humidityCompare(optionsHumidity, resultHumidity);
            humidity = true;
        }

        String optionsPh = options.getValue("soil_ph")[0];
        String resultPh = queryResult.getValue("soil_ph")[0];
        double phSimilarity = 1.0;
        boolean ph = false;
        if (!optionsPh.equals("")) {
            phSimilarity = soilPhCompare(optionsPh, resultPh);
            ph = true;
        }

        double totalSimilarity = lightSimilarity * soilSimilarity * waterSimilarity * tempSimilarity * potSizeSimilarity
                * plantCareSimilarity * humiditySimilarity * phSimilarity;

        ArrayList<String> attributeNamesNewJSON = new ArrayList<String>();
        attributeNamesNewJSON.add("similarity");
        attributeNamesNewJSON.add("lightComment");
        attributeNamesNewJSON.add("soilComment");
        attributeNamesNewJSON.add("waterComment");
        attributeNamesNewJSON.add("tempComment");
        attributeNamesNewJSON.add("potSizeComment");
        attributeNamesNewJSON.add("plantCareComment");

        // if(humidity){
        attributeNamesNewJSON.add("humidityComment");
        // }

        // if(ph){
        attributeNamesNewJSON.add("phComment");
        // }

        attributeNamesNewJSON.add("sproutTime");
        attributeNamesNewJSON.add("vegetativeTime");
        attributeNamesNewJSON.add("floweringTime");
        attributeNamesNewJSON.add("matureTime");
        attributeNamesNewJSON.add("botanic_category");

        String[] attributeNamesNewJSONArray = new String[attributeNamesNewJSON.size()];

        for (int n = 0; n < attributeNamesNewJSONArray.length; n++) {
            attributeNamesNewJSONArray[n] = attributeNamesNewJSON.get(n);
        }

        ArrayList<String> attributeValuesNewJSON = new ArrayList<String>();

        attributeValuesNewJSON.add(Double.toString(totalSimilarity));

        if (lightSimilarity != 1) {
            attributeValuesNewJSON.add("The best light option is " + allLights + ", you selected " +  optionsLight + ".");
            
        } else{
            attributeValuesNewJSON.add("");
             }

        if (soilSimilarity != 1) {
            attributeValuesNewJSON.add("The best soil is " + queryResult.getValue("soil")[0] + ", you selected " + optionsSoil + ".");
        } else {
            attributeValuesNewJSON.add("");
        }

        if (waterSimilarity != 1) {
            int preferredFreq = (int)(Double.parseDouble(queryResult.getValue("preferred_watering_frequency")[0])/24);
            System.out.println(preferredFreq);
            int selectedFreq = (int)(Double.parseDouble(optionsWater)/24);
            System.out.println(selectedFreq);
            attributeValuesNewJSON.add("The best watering frequency is every " + preferredFreq + " day(s)" + ", you selected every " + selectedFreq + " day(s).");
        } else {
            attributeValuesNewJSON.add("");
        }

        if (tempSimilarity != 1) {
            attributeValuesNewJSON.add("The best temperature is inbetween " + queryResult.getValue("preferred_average_temperature")[0]+"C" + ", you selected " + optionsTemp + ".");
        } else {
            attributeValuesNewJSON.add("");
        }

        if (potSizeSimilarity != 1) {
            attributeValuesNewJSON.add("The best pot size is " + queryResult.getValue("preferred_pot_size")[0] + ", you selected " + optionsPotsize + ".");
        } else {
            attributeValuesNewJSON.add("");
        }
        


        System.out.println("plantCareSimilarity is " + plantCareSimilarity);
        if (plantCareSimilarity != 1.1) {
            if(optionsPlantCare[0] != "" && optionsPlantCare != null){
                attributeValuesNewJSON.add("Instead of using the plant care choice(s) " + stringArrayToString(options.getValue("plant_care")) + ", consider using the plantcare choice(s) " + stringArrayToString(queryResult.getValue("plant_care")) + ".");
            } else {
                attributeValuesNewJSON.add("Instead of using no plant care, consider using the plant care choice(s) " + stringArrayToString(queryResult.getValue("plant_care")) + ".");
            }
        } else {
            attributeValuesNewJSON.add("");
        }

        if (humidity) {
            if (humiditySimilarity != 1) {
                attributeValuesNewJSON.add("The best humidity level is inbetween " + resultHumidity + "%" + ", you selected " + optionsHumidity + "%.");
            } else {
                attributeValuesNewJSON.add("");
            }
        } else {
            attributeValuesNewJSON.add("");
        }

        if (ph) {
            if (phSimilarity != 1) {
                attributeValuesNewJSON.add("The best soil pH is " + resultPh + ", you selected " + optionsPh + ".");
            } else {
                attributeValuesNewJSON.add("");
            }
        } else {
            attributeValuesNewJSON.add("");
        }

        attributeValuesNewJSON.add(queryResult.getValue("sprout")[0]);
        attributeValuesNewJSON.add(queryResult.getValue("vegetative")[0]);
        attributeValuesNewJSON.add(queryResult.getValue("flowering")[0]);
        attributeValuesNewJSON.add(queryResult.getValue("mature")[0]);

        attributeValuesNewJSON.add(queryResult.getValue("botanic_category")[0]);

        String[] outArrayPreliminary = new String[attributeValuesNewJSON.size()];

        for (int n = 0; n < outArrayPreliminary.length; n++) {
            outArrayPreliminary[n] = attributeValuesNewJSON.get(n);
        }

        String[][] outArrayFinal = new String[outArrayPreliminary.length][];

        for (int n = 0; n < outArrayFinal.length; n++) {
            outArrayFinal[n] = new String[1];
            outArrayFinal[n][0] = outArrayPreliminary[n];
        }

        JSONObject outJSON = new JSONObject(attributeNamesNewJSONArray, outArrayFinal);

        return outJSON;
    }

    private String stringArrayToString(String[] array){
        String out = "";
        for(int n = 0; n < array.length; n++){
            out += array[n] + ", ";
        }

        out = out.substring(0, out.length()-2);
        return out;
    }

    public double lightCompare(String light1, String light2) {

        double res = 0;

        if (light1.equals(light2)) {
            res = 1.0;
        }

        if (light1.equals("full sun") && light2.equals("partial sun")) {
            res = 0.75;
        }

        if (light1.equals("full sun") && light2.equals("shade")) {
            res = 0.0;
        }

        if (light1.equals("partial sun") && light2.equals("shade")) {
            res = 0.0;
        }

        return res;

    }

    public double soilCompare(String soil1, String soil2) {

        double soilRes = 0;

        if (soil1.equals(soil2)) {
            soilRes = 1.0;
        }

        if (soil1.equals("well-draining") && soil2.equals("Loamy soil")) {
            soilRes = 0.5;
        }

        if (soil1.equals("well-draining") && soil2.equals("Potting mix")) {
            soilRes = 0.5;
        }
        return soilRes;

    }

    public double waterCompare(String water1, String water2) {

        try {
            double waterRatio = Double.valueOf(water1) / Double.valueOf(water2);

            if (waterRatio > 1) {
                waterRatio = 1 / waterRatio;
            }

            double out = 1 - Math.pow(waterRatio - 1, 2) * 2;

            if (out < 0) {
                out = 0;
            }

            out = out * 100;

            int outInt = (int) Math.round(out);

            out = (double) outInt / 100;

            return out;
        } catch (NumberFormatException e) {
            System.out.println("Wrong stuff from database or frontend");
            return 0;
        }
    }

    public double temperatureCompare(String temp1String, String temp2String) {
        int temp1 = Integer.valueOf(temp1String);

        String[] temp2StringArray = temp2String.split("-");

        int lowerRange = Integer.valueOf(temp2StringArray[0]);
        int upperRange = Integer.valueOf(temp2StringArray[1]);

        double difference = 0.0;

        if (lowerRange <= temp1 && temp1 <= upperRange) {
            difference = 0;
        } else {
            if(temp1 < lowerRange){
                difference = lowerRange - temp1;
            }

            if(upperRange < temp1){
                difference = temp1 - lowerRange;
            }
        }

        double out = 1 - difference/20;

        return out;
    }

    public double potSizeCompare(String size1, String size2) {

        if (size1.equals(size2) || size2.equals("any")) {
            return 1.0;
        }

        int size1Int = sizeAsInt(size1);

        int size2Int = sizeAsInt(size2);

        int sizeDifference = size1Int - size2Int;

        if (sizeDifference <= 0) {
            if (sizeDifference == -1) {
                return 0.9;
            }

            if (sizeDifference == -2) {
                return 0.7;
            }

            if (sizeDifference == -3) {
                return 0.4;
            }

            if (sizeDifference == -4) {
                return 0.0;
            }

            return 0.0;

        } else {
            return 1.0;
        }
    }

    private int sizeAsInt(String size) {

        if (size.equals("extra small")) {
            return 1;
        }
        if (size.equals("small")) {
            return 2;
        }

        if (size.equals("medium")) {
            return 3;
        }
        if (size.equals("large")) {
            return 4;
        }
        if (size.equals("extra large")) {
            return 5;
        }

        return 0;
    }


 
    public double plantCareCompareSeveralNew(String[] care1, String[] care2){
        //Provided both strings have elements
        if(care1 != null && care1[0] != "" && care2 != null && care2[0] != ""){
                double matchCount = 0;

                for(int n = 0; n < care1.length; n++){
                    for(int i = 0; i < care2.length; i++){
                        if(care1[n].equals(care2[i])){
                            matchCount++;
                        }
                    }
                }

                System.out.println("MATCHCOUNT: " + matchCount);

                double matchRatio = matchCount / care2.length;

                return 1 + matchRatio/10;
            } else {
                return 1;
            }
        }


        public double plantCareCompareSeveral(String[] care1, String[] care2) {
            double res = 1;
            int length1 = care1.length;
            int length2 = care2.length;

            if(length1 != length2){
                for (int n = 0; n < care1.length; n++) { 
                    
                    if(!care1[n].equals(care2[0])){
                        res = res - 0.05;
                    }else{
                        indexValueOfMatchingElements = n;
                    }
                }
            }

            if(length1 == length2){
                if((care1[0].equals(care2[0]))){
                    return res;
                }
                else{
                    res = res - 0.05;
                }
            }
        return res;
    }
    
           

        /*if (length1 > 0 && length1 < length2) {// Kommer inte ske ifall databasen bara har ett värde
            for (int i = 0; i < length2; i++) {
                if (!care1[i].equals(care2[i])) {
                    res = res - 0.1;
                }
            }
        }

        if (length1 > length2) {
            for (int i = 0; i < length2; i++) {
                if (!care1[i].equals(care2[i])) {
                    res = res - 0.1;
                }
            }
        }

        return res;
        }
    */

    public double humidityCompare(String humidity1String, String humidity2String) {
        int humidity1 = Integer.valueOf(humidity1String);
        // B)
        String[] humidity2StringArray = humidity2String.split("-");

        int lowerRange = Integer.valueOf(humidity2StringArray[0]);
        int upperRange = Integer.valueOf(humidity2StringArray[1]);

        double difference = 0;

        if (lowerRange <= humidity1 && humidity1 <= upperRange) {
            difference = 0;
        } else {
            if(humidity1 < lowerRange){
                difference = lowerRange - humidity1;
            }

            if(upperRange < humidity1){
                difference = humidity1 - upperRange;
            }
        }

        double out = 1 - difference / 500;

        return out;
    }

    public double soilPhCompare(String ph1String, String ph2String) {

        double ph1 = Double.valueOf(ph1String);
        // B)
        String[] ph2StringArray = ph2String.split("-");

        double lowerRange = Double.valueOf(ph2StringArray[0]);
        double upperRange = Double.valueOf(ph2StringArray[1]);

        double difference = 0;

        if (lowerRange <= ph1 && ph1 <= upperRange) {
            difference = 0;
        } else {
            if(ph1 < lowerRange){
                difference = lowerRange - ph1;
            }

            if(upperRange < ph1){
                difference = ph1 - upperRange;
            }
        }

        double out = 1 - difference / 3;

        return out;
    }

}
