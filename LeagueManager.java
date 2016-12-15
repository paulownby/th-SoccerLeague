import com.teamtreehouse.OrganizerMachine;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;

public class LeagueManager {
  
  public static void main(String[] args) {
    Player[] players = Players.load();
    OrganizerMachine machine = new OrganizerMachine(players);
    machine.run();
  }

}

