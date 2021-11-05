package tech.codingclub.helix.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.codingclub.helix.global.HttpURLConnectionExample;

import java.util.Date;

public class WikipediaDownloader{

    private String keyword;
    public WikipediaDownloader(){

    }

    public WikipediaDownloader(String keyword) {
        this.keyword = keyword;
    }

    public WikiResult getResult() {

        //1. Get clean keyword !
        //2. Get the url for Wikipedia
        //3. Make a GET request to wikipedia !
        //4. Parsing the useful results using jsoup
        //5. Showing results !

        if(this.keyword==null || this.keyword.length() ==0 ){
            return null;
        }

        //STEP 1
        this.keyword = this.keyword.trim().replaceAll("[ ]+","_");

        //STEP 2
        String wikiUrl = getWikipediaUrlForQuery(this.keyword);
        String response = "";
        String imageUrl = null;

        try {

            //STEP 3
            String wikipediaResponseHTML = HttpURLConnectionExample.sendGet(wikiUrl);// CTL + P
            // System.out.println(wikipediaResponseHTML);

            //STEP 4
            Document document = Jsoup.parse( wikipediaResponseHTML ,"https://en.wikipedia.org");

            Elements childElements = document.body().select(".mw-parser-output > *");

            int state = 0;

            for(Element childElement : childElements){
                if(state==0){
                    if(childElement.tagName().equals("table")){
                        state = 1;
                    }
                }else if(state == 1){
                    if(childElement.tagName().equals("p")){
                        state = 2;
                        response = childElement.text();
                        break;
                    }
                }
            }

            try {
                imageUrl = document.body().select(".infobox img").get(0).attr("src");
            }catch (Exception ex){

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        WikiResult wikiResult = new WikiResult(this.keyword, response, imageUrl);
        // PUSH RESULT INTO DATABASE

        return wikiResult;
    }

    private String getWikipediaUrlForQuery(String cleanKeyword) {
        return "https://en.wikipedia.org/wiki/"+cleanKeyword;
    }
}
