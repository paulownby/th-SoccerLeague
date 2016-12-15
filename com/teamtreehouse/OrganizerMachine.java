package com.teamtreehouse;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.model.Teams;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


public class OrganizerMachine {
  private Map<String, String> mMenu;
  private Player[] mPlayers;
  private Teams mTeams;
  private BufferedReader mReader;
  private String mVersion;
  
  public OrganizerMachine (Player[] players) {
    mVersion = "map";
    mPlayers = players;
    mTeams = new Teams();
    mReader = new  BufferedReader(new InputStreamReader(System.in));
    mMenu = new LinkedHashMap<String, String>();
    mMenu.put("create", "Create a new team");
    mMenu.put("add", "Add players to teams.");
    mMenu.put("remove", "Remove players from teams.");
    mMenu.put("roster", "Show a list of players on a team.");
    mMenu.put("height", "Show a team's height report.");
    mMenu.put("balance", "Show the League Balance Report.");
    mMenu.put("quit", "Exit the program");
    
  }
  private void clearScreen() {
    System.out.print("\033[H\033[2J");
  }
  private void promptNoTeamsCreatedYet() throws IOException {
    System.out.printf("There are no teams created yet. Please create a team first.");
    promptReturnToMenu();
  }
  private void promptReturnToMenu() throws IOException {
    String key;
    System.out.printf("%n%nPress enter to return to the main menu:  ");
    key = mReader.readLine();
  }
  
  private String promptAction () throws IOException {
    System.out.printf("There are currently %d of %d registered players assigned to teams.%n%n"+
                      "Your options are: %n%n",
                      mTeams.getAssignedPlayers().size(),
                      mPlayers.length);
    for (Map.Entry<String, String> option : mMenu.entrySet()) {
      System.out.printf("%s - %s %n", option.getKey(), option.getValue());
    }
    System.out.printf("%nWhat would you like to do:  ");
    String choice = mReader.readLine();
    return choice.trim().toLowerCase();
  }
  private Team promptNewTeam() throws IOException {
    String teamName = "";
    while (teamName.length() == 0) {
      System.out.print("Enter the new team's name:  ");
      teamName = mReader.readLine();
      if (teamName.length() == 0) {
        System.out.printf("%nYou have to name the team something. ");
      }
      if(mTeams.getTeamNames().contains(teamName)) {
        System.out.printf("%n%s already exists. ",teamName);
        teamName = "";
      }
    }
    String coach = "";
    while (coach.length() == 0) {
      System.out.printf("Enter the coach's name for the team %s:  ", teamName);
      coach = mReader.readLine();
      if (coach.length() == 0) {
        System.out.printf("%nYou have to name the coach something. ");
      }
    }
    
    return new Team(teamName, coach);
  }
  
  private Team promptForTeam(String action) throws IOException {
    System.out.printf("Here are the available teams to %s:%n%n", action);    
    List<String> teams = new ArrayList<>(mTeams.getTeamNames());
    int index = promptForIndex(teams);
    return mTeams.getTeamByName(teams.get(index));
  }
  private int promptForIndex(List<String> options) throws IOException {
    int counter = 1;
    int choice = 0;
    boolean isInteger = false;
    String optionAsString;
    
    for (String option : options) {
      System.out.printf("%3d) %s%n", counter, option);
      counter++;
    }
    System.out.printf("%nPlease enter the number for your selection:  ");
    while (choice < 1 || choice > counter-1 || !isInteger) {
      optionAsString = mReader.readLine();
      try {
        choice = Integer.parseInt(optionAsString.trim());
        isInteger = true; 
      } catch (IllegalArgumentException iae) {
        isInteger = false;
      }
      if (choice < 1 || choice > counter-1 || !isInteger) {
        System.out.printf("%nPlease make a valid selection from the list above:  ");
      }
    }
    return choice-1;
  }
  private Player promptForPlayer(Collection collection, String action) throws IOException {
    System.out.printf("Here are the available players to %s:%n%n", action);    
    System.out.printf("   Full Name                   Height  Experience%n");
    System.out.printf("   =========================   ======  ==========%n");
    List<Player> players = new ArrayList<>(collection);
    List<String> playersAsStrings = new ArrayList<>();
    for (Player player : players) {
      playersAsStrings.add(player.toString());
    }
    int index = promptForIndex(playersAsStrings);
    return players.get(index);

  }
  
  private TreeSet<Player> getAvailablePlayers() {
    TreeSet<Player> availablePlayers = new TreeSet<Player>(Arrays.asList(mPlayers));

    for (Team team : mTeams.getTeams()) {
      if (team.getRoster() != null) {
        availablePlayers.removeAll(team.getRoster());
      }
    }
    return availablePlayers;
  }
  private void showTeamRoster(Team team) {
    System.out.printf("Team Roster%n");
    System.out.printf("Team: %s%n", team);
    System.out.printf("Full Name                   Height  Experience%n");
    System.out.printf("=========================   ======  ==========%n");
    for (Player eachPlayer : team.getRoster()) {
      System.out.printf("%s%n",eachPlayer.toString());
    }
  }
  private void showTeamHeightReport(Team team) {
    Map<String, List<Player>> heightMap = new HashMap<>();
    int height;
    int maxCount = 0;
    String teamName = team.getTeamName();
    
    for (Team loopTeam : mTeams.getTeams()) {
      TreeSet<Player> roster = loopTeam.getRoster();
      String loopTeamName = loopTeam.getTeamName();
      heightMap.put(loopTeamName+"35-40", new ArrayList<>());
      heightMap.put(loopTeamName+"41-46", new ArrayList<>());
      heightMap.put(loopTeamName+"47-50", new ArrayList<>());
      for (Player player : roster) {
        height = player.getHeightInInches();
        if (height < 41) {
          heightMap.get(loopTeamName+"35-40").add(player);
        } else if (height > 46) {
          heightMap.get(loopTeamName+"47-50").add(player);
        } else {
          heightMap.get(loopTeamName+"41-46").add(player);
        }
      }
    }
    
    
    System.out.printf("Height Report%n"+
                      "Team: %s%n",team);
    System.out.printf("         35 - 40                    41 - 46                    47 - 50%n");
    System.out.printf("=========================  =========================  =========================%n");
    if (heightMap.get(teamName+"35-40").size()>maxCount) maxCount = heightMap.get(teamName+"35-40").size();
    if (heightMap.get(teamName+"41-46").size()>maxCount) maxCount = heightMap.get(teamName+"41-46").size();
    if (heightMap.get(teamName+"47-50").size()>maxCount) maxCount = heightMap.get(teamName+"47-50").size();
    for (int i=0;i<maxCount;i++) {
      if (i+1>heightMap.get(teamName+"35-40").size()) {
        System.out.printf("%1$-25s  "," ");
      } else {
        System.out.printf("%1$-25s  ",heightMap.get(teamName+"35-40").get(i).getNameAndHeight());
      }
      if (i+1>heightMap.get(teamName+"41-46").size()) {
        System.out.printf("%1$-25s  "," ");
      } else {
        System.out.printf("%1$-25s  ",heightMap.get(teamName+"41-46").get(i).getNameAndHeight());
      }
      if (i+1>heightMap.get(teamName+"47-50").size()) {
        System.out.printf("%1$-25s%n"," ");
      } else {
        System.out.printf("%1$-25s%n",heightMap.get(teamName+"47-50").get(i).getNameAndHeight());
      }
    }
    System.out.printf("-------------------------  -------------------------  -------------------------%n");
    System.out.printf("Counts:    %1$2s                         %2$2s                         %3$2s%n%n",
                      String.format("%d",heightMap.get(teamName+"35-40").size()),
                      String.format("%d",heightMap.get(teamName+"41-46").size()),
                      String.format("%d",heightMap.get(teamName+"47-50").size()));
    for (Team otherTeam : mTeams.getTeams()) {
      if (!otherTeam.equals(team)) {
        String otherTeamName = otherTeam.getTeamName();
        System.out.printf("%s Counts:%n",otherTeamName);
        System.out.printf("           %1$2s                         %2$2s                         %3$2s%n%n",
                        String.format("%d",heightMap.get(otherTeamName+"35-40").size()),
                        String.format("%d",heightMap.get(otherTeamName+"41-46").size()),
                        String.format("%d",heightMap.get(otherTeamName+"47-50").size()));
      }
    }
  }
  private void showLeagueBalanceReport() {
    int maxTeamNameLength = mTeams.getMaxTeamNameLength();
    if (maxTeamNameLength < 9) maxTeamNameLength = 9;
    String teamNameUnderline = String.format("%1$-"+maxTeamNameLength+"s","=").replace(" ","=");
    System.out.printf("League Balance Report%n%n"+
                      "%1$-"+maxTeamNameLength+"s                   Not        Percent%n"+
                      "%2$-"+maxTeamNameLength+"s  Experienced  Experienced  Experienced%n"+
                      teamNameUnderline+        "  ===========  ===========  ===========%n",
                      " ","Team Name");
    for (Team team : mTeams.getTeams()) {
      int playerCount = team.getPlayerCount();
      double experienceAverage;
      if (playerCount == 0) {
        experienceAverage = 0;
      } else {
        experienceAverage = 100.0*(double)team.byExperience().get("experienced").size()/(double)team.getPlayerCount();
      }
      System.out.printf("%1$-"+maxTeamNameLength+"s      %2$2s           %3$2s          %4$5s%n",
                        team.getTeamName(), 
                        String.format("%d",team.byExperience().get("experienced").size()), 
                        String.format("%d",team.byExperience().get("not experienced").size()), 
                        String.format("%.1f",experienceAverage));
                        
    }
  }  
  public void run() {
    String choice = "";
    Team team;
    Player player;
    clearScreen();
    do {
      try {
        clearScreen();
//        System.out.printf("Version: %s%n",mVersion);
        choice = promptAction();
        switch(choice) {
          case "create":
            clearScreen();
            if (mTeams.getTeamCount()>=mPlayers.length/11) {
              System.out.printf("You cannot have any more than %d teams.",
                                mTeams.getTeamCount());
              promptReturnToMenu();
              break;
            }
            team = promptNewTeam();
            mTeams.addTeam(team);
            System.out.printf("%s added!", team);
            promptReturnToMenu();
            break;
          case "add":
            clearScreen();
            if (mTeams.getTeamCount() == 0) {
              promptNoTeamsCreatedYet();
              break;
            } 
            team = promptForTeam("add players");
            clearScreen();
            if (team.getPlayerCount() > 10) {
              System.out.printf("%n%n%s is full and cannot receive new players.",
                                team);
              promptReturnToMenu();
              break;
            }
            if (getAvailablePlayers().size() == 0) {
              System.out.printf("There are no more players to add.%n%n", team.getTeamName());
              promptReturnToMenu();
              break;  
            }
            player = promptForPlayer(getAvailablePlayers(),"add");
            team.addPlayer(player);
            System.out.printf("%n%s %s was added to the team %s.",
                              player.getFirstName(),
                              player.getLastName(),
                              team.getTeamName());
            promptReturnToMenu();
            break;
          case "remove":
            clearScreen();
            if (mTeams.getTeamCount() == 0) {
              promptNoTeamsCreatedYet();
              break;
            }
            team = promptForTeam("remove players");
            clearScreen();
            if (team.getPlayerCount() == 0) {
              System.out.printf("%s has no players to remove.%n%n", team.getTeamName());
              promptReturnToMenu();
              break;
            }
            player = promptForPlayer(team.getRoster() ,"remove");
            team.removePlayer(player);
            System.out.printf("%n%s %s was removed from the team %s.",
                             player.getFirstName(),
                             player.getLastName(),
                             team.getTeamName());
            promptReturnToMenu();
            break;
          case "roster":
            clearScreen();
            if (mTeams.getTeamCount() == 0) {
              promptNoTeamsCreatedYet();
              break;
            }
            team = promptForTeam("show the roster");
            clearScreen();
            showTeamRoster(team);
            promptReturnToMenu();
            break;
          case "height":
            clearScreen();
            if (mTeams.getTeamCount() == 0) {
              promptNoTeamsCreatedYet();
              break;
            }
            team = promptForTeam("show the height report");
            clearScreen();
            showTeamHeightReport(team);
            promptReturnToMenu();
            break;
          case "balance":
            clearScreen();
            showLeagueBalanceReport();
            promptReturnToMenu();
            break;
          case "quit":
            break;
          default:
            clearScreen();
            System.out.printf("Unknown choice:  '%s'. Please try again.%n%n", choice);
            promptReturnToMenu();
        }
      } catch (IOException ioe) {
        System.out.println("Problem with input");
        ioe.printStackTrace();
      }
    } while (!choice.equals("quit"));
  }
}