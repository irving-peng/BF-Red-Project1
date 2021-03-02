// --== CS400 File Header Information ==--
// Name: Srikar Vootkur
// Email: svootkur@wisc.edu
// Team: BF - Red
// Role: Frontend
// TA: BRIANNA COCHRAN
// Lecturer: GARY DAHL
// Notes to Grader: <optional extra notes>

import java.io.*;
import java.util.*;

public class Frontend {
	
	public Frontend() {
        
	}	  
	
    private static boolean isInt(String s) {
        if (s == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args){
        Backend be;
        String path = args[0];
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row = csvReader.readLine();
        String data = "";
        while (row != null) {
            data = data + row + "\n";
            row = csvReader.readLine();
        }
        csvReader.close();
        be = new Backend(data);

        //Scanner to get Input from User 
        Scanner in = new Scanner(System.in); 

        //select all ratings initially
        for(int i=0; i<11; i++){
            be.addAvgRating(String.valueOf(i));
        }

        boolean exit = false;
        int step = 1;
        List<String> genres = be.getAllGenres();
        String mode = "b";
        List<MovieInterface> movies = be.getThreeMovies(0);
        for(int m = 0; m<movies.size(); m++){
            System.out.println((m+1) + ". " + movies.get(m));
        }
        String inp = in.nextLine();
        while(exit==false){
            
            //check mode
            
            while(mode=="b"){

                System.out.println("Welcome! Here are the top three movies. Type in a number to see the movie at that rank (by rating) and the succeeding two movies. Click 'g' to select genre(s) OR click 'r' to select a rating to filter by OR click 'x' to exit the program.");
                inp = in.nextLine();

                if(isInt(inp)){
                    movies = be.getThreeMovies(Integer.parseInt(inp));
                    for(int m = 0; m<movies.size(); m++){
                        System.out.println((m+1) + ". " + movies.get(m));
                    }
                }
                if(inp=="x"){
                    exit = true;
                    in.close();
                }
                else if(inp == "g"){
                    mode = "g";
                }
                else if(inp == "r"){
                    mode = "r";
                }
                else{
                    System.out.println("Please enter a valid command.");
                    continue;
                }
            }

            while(mode=="g"){
                System.out.println("Genres with '---' before the number indicates an already selected genre. Choose a number by a genre name that is unselected to select that genre. Choose a number by a genre name that is selected to unselect that genre. Input 'x' to return to the main movies list.");

                //display
                genres = be.getAllGenres();
                for(int g = 0; g<genres.size(); g++){
                    if(be.getGenres().contains(genres.get(g))){
                        System.out.println("---" + (g+1) + ". " + genres.get(g));
                    }
                    else{
                        System.out.println((g+1) + ". " + genres.get(g));
                    }
                }
                
                inp = in.nextLine();
                while(isInt(inp)){
                    
                    String gen = genres.get(Integer.parseInt(inp)-1));
                    if(!be.getGenres().contains(gen){
                        be.addGenre(gen);
                    }
                    else{
                        be.removeGenre(gen);
                    }
                    inp = in.nextLine();
                    System.out.println("To return to the main movies list, click 'x'");
                }
                if(inp == "x"){
                    mode="b";
                }  
                else{
                    System.out.println("Please enter a valid command.");
                    continue;
                }  
            }

            while(mode=="r"){
                System.out.println("Ratings with '---' before the number indicates an already selected rating range. Choose a number by a rating range that is unselected to select that rating range. Choose a number by a rating name that is selected to unselect that rating name. Input 'x' to return to the main movies list.");

                //display
                for(int r = 0; r<10; r++){
                    if(be.getAvgRatings().contains(String.valueOf(r))){
                        System.out.println("---" + (r) + ". " + r + " to " + (r+1));
                    }
                    else{
                        System.out.println((r) + ". " + r + " to " + (r+1));
                    }
                }
                if(be.getAvgRatings().contains("10")){
                    System.out.println("---10. 10 to 10");
                }
                else{
                    System.out.println("10. 10 to 10");
                }
                
                inp = in.nextLine();
                while(isInt(inp)){
                    
                    String gen = genres.get(Integer.parseInt(inp)-1));
                    if(!be.getGenres().contains(gen){
                        be.addGenre(gen);
                    }
                    else{
                        be.removeGenre(gen);
                    }
                    inp = in.nextLine();
                    System.out.println("To return to the main movies list, click 'x'");
                }
                if(inp == "x"){
                    mode="b";
                }
                else{
                    System.out.println("Please enter a valid command.");
                    continue;
                }  
            }
        
        }

    }
}
