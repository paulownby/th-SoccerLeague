package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Teams {
  private TreeSet<Team> mTeams;
  
  public Teams() {
    mTeams = new TreeSet<Team>();
  }
  
  public void addTeam(Team team) {
    mTeams.add(team);
  }

  public int getTeamCount() {
    return mTeams.size();
  }
  
  private Map<String, Team> byTeamName() {
    Map<String, Team> byTeamName = new TreeMap<>();
    for (Team team : mTeams) {
      byTeamName.put(team.mTeamName, team);
    }
    return byTeamName;
  }
  public int getMaxTeamNameLength() {
    int maxLength = 0;
    for (String team : getTeamNames()) {
      if (team.length() > maxLength) {
        maxLength = team.length();
      }
    }
    return maxLength;
  }
  
  public Set<Team> getTeams() {
    return mTeams;
  }
  
  public List<String> getTeamNames() {
    
    List<String> teams =  new ArrayList(byTeamName().keySet());
    teams.sort(new Comparator<String>() {
      @Override
      public int compare(String team1, String team2) {
        if (team1.compareToIgnoreCase(team2)!=0) {
          return team1.compareToIgnoreCase(team2);
        }
        return team1.compareTo(team1);
      }
    });
    return teams;
    
    
    
    
    
    
    
//    return byTeamName().keySet();
  }
  public Team getTeamByName(String teamName) {
    return byTeamName().get(teamName);
  }
  
  public List<Player> getAssignedPlayers() {
    List<Player> assignedPlayers = new ArrayList<>();
    for (Team team : mTeams) {
      if (team.getRoster() != null) {
        assignedPlayers.addAll(team.getRoster());
      }
    }
    return assignedPlayers;
  }
}
