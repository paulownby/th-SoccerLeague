package com.teamtreehouse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Team implements Comparable<Team> {
  protected String mTeamName;
  protected String mCoach;
  protected TreeSet<Player> mRoster;
  
  public Team (String teamName, String coach) {
    mTeamName = teamName;
    mCoach = coach;
    mRoster = new TreeSet<Player>();
  }

  public void addPlayer(Player player) {
    mRoster.add(player);
  }
  public String getTeamName() {
    return mTeamName;
  }
  
  public int getPlayerCount() {
      return mRoster.size();
  }
  
  public Map<String, List<Player>> byExperience() {
    Map<String, List<Player>> byExperience = new TreeMap<>();
    for (Player player : mRoster) {
      List<Player> experiencePlayers;
      if (player.isPreviousExperience()) {
        experiencePlayers = byExperience.get("experienced");
      } else {
        experiencePlayers = byExperience.get("not experienced");
      }
      if (experiencePlayers == null) {
        experiencePlayers = new ArrayList<>();
        if (player.isPreviousExperience()) {
          byExperience.put("experienced", experiencePlayers);
        } else {
          byExperience.put("not experienced", experiencePlayers);
        }  
      }
      experiencePlayers.add(player);
    }
    return byExperience;
  }

  
  
  public int getExperienceCount() {
    int experienceCount = 0;
    for (Player player : mRoster) {
      if (player.isPreviousExperience()) experienceCount++;
    }
    return experienceCount;
  }
  
  public void removePlayer(Player player) {
    mRoster.remove(player);
  }
  public TreeSet<Player> getRoster() {
    TreeSet<Player> roster = new TreeSet(mRoster);
    return roster;
  }
  
  @Override
  public int compareTo(Team other) {
    if (equals(other)) return 0;
    if (toString().compareToIgnoreCase(other.toString())!=0) {
      return toString().compareToIgnoreCase(other.toString());
    } else {
      return toString().compareTo(other.toString());
    }
    
//    return getTeamName().compareTo(other.getTeamName());
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Team)) return false;

    Team team = (Team) o;

    if (mTeamName != team.mTeamName) return false;
    if (mCoach != team.mCoach) return false;
    return mCoach.equals(team.mCoach);

  }

  
  @Override
  public String toString() {
    return String.format("%s (coached by %s)", mTeamName, mCoach);
  }
}
