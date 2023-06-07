import java.util.*;

public class SpamDetector {

    private static final double SIMILARITY_THRESHOLD = 0.6;

    public static void main(String... args) {
        List<String> emailBodyTexts = getEmailTexts();

        Map<String,List> spamResuls =  scanMails(emailBodyTexts);
        for (Map.Entry<String,List> email:spamResuls.entrySet()){
            String userEmail = email.getKey();
            List spamStatus = email.getValue();
            System.out.println("Email Body: " + userEmail);
            System.out.println("Spam Probability: " +  spamStatus.get(0));
            System.out.println("Email match count: "+ spamStatus.get(1));
            System.out.println("Spam ?: "+ ((double) spamStatus.get(0) >= SIMILARITY_THRESHOLD));
            System.out.println("--------------------");
            System.out.println();
        }

    }


    private static List<String> getEmailTexts() {
        List<String> BodyTexts = new ArrayList<>();
        BodyTexts.add("Hello,Hello notice this is a legitimate email.");
        BodyTexts.add("URGENT: email You have won a prize! Click here to claim.");
        BodyTexts.add("Hi, just checking in. How are you?");
        BodyTexts.add("Important notice: Your account has been compromised.");
        BodyTexts.add("Hello Fredy this is Test mail from ddd");
        return BodyTexts;
    }

    private static Map<String,List> scanMails(List<String> emailBodyTexts){
       long bodyTextsLength = emailBodyTexts.size();
       Map<String,List> spamResult = new HashMap<>();


        for (int i=0;i<bodyTextsLength;i++){
            double similaritySum = 0.0;
            int numSimilarEmails = 0;
            String emailText = emailBodyTexts.get(i);
            for (int j=0;j<bodyTextsLength;j++){
                if(i==j){
                    continue;
                }
                String otherEmailText = emailBodyTexts.get(j);
                double matchingCount =  calculateSimilarity(emailText.toLowerCase(),otherEmailText.toLowerCase());
                similaritySum+=matchingCount;
                numSimilarEmails = matchingCount>0 ? numSimilarEmails+1 : numSimilarEmails;

            }
            spamResult.put(emailBodyTexts.get(i),List.of(similaritySum,numSimilarEmails));
        }
        return spamResult;
    }


    private static double calculateSimilarity(String bodyOne,String bodyTwo){
        List<String> bodyOneWords = new ArrayList<>(Arrays.asList(bodyOne.split("\\W+")));
        List<String> bodyTwoWords = new ArrayList<>(Arrays.asList(bodyTwo.split("\\W+")));
        int matchingWordsCount = 0;

        for (String word:bodyOneWords){
            if(bodyTwoWords.contains(word)){
                matchingWordsCount++;
            }
        }

        double similarity = (double) matchingWordsCount / Math.sqrt(bodyOneWords.size() * bodyTwoWords.size());
        return similarity;

    }

}