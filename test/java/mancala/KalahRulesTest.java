package mancala;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class KalahRulesTest {
    private KalahRules rules;
    private Player player1;
    private Player player2;
    private MancalaDataStructure data;

    @BeforeEach
    public void setUp() {
        /*will be making the assumption that the constructor call
        sets everything up to call other methods */
        rules = new KalahRules(); 
        player1 = new Player();
        player2 = new Player();
        /*don't change the register players method signature
        because I need players to test */
        rules.registerPlayers(player1, player2); 
        data = rules.getDataStructure();
    }


 
    @Test
    public void testMoveStonesOneMove() throws InvalidMoveException {
    
        //player one move stones starting at pit 6
        int num = rules.moveStones(6,1);
        assertEquals(1,num);  //number in players store
        //pit 6 has zero stones
        assertEquals(0,data.getNumStones(6));
        //pit 8 has 5 stones
        assertEquals(5,data.getNumStones(8));
}

    @Test
    public void testMoveStonesWithCapture() throws InvalidMoveException {
  
        /*
        The move we will be testing is to pull the stones from pit 6
        and distribute them.  We will set up so that there enough stones in 
        pit 6 to wrap around to pit 1, which we will empty before starting 

        */
        //add stones to pit 6 to total 8 stones
        data.addStones(6,4);
        //remove all stones from pit 1
        data.removeStones(1);
        //player one move stones starting at pit one
        int num = rules.moveStones(6,1);
       
       //player store should now have 7 stones
       assertEquals(7, player1.getStoreCount());    
       //pits 1 and 12 should be empty
       assertEquals(0,data.getNumStones(1));
       assertEquals(0,data.getNumStones(12));       
  }

    @Test
    public void testDistributeStonesSingleMove() {
  
        //distribute stones starting at pit 6
        int num = rules.distributeStones(6);
        assertEquals(4,num);  //number distributed
        //pit 6 has zero stones
        assertEquals(0,data.getNumStones(6));
        //pit 8 has 5 stones
        assertEquals(5,data.getNumStones(8));
}


    @Test
    public void testCaptureStonesNonEmptyTarget() {
  
         //remove all stones from pit 1
        data.removeStones(1);
        // add a single stone to pit 1 to simulate capture scenario
        data.addStones(1,1);
        //player one move stones starting at pit one
        rules.setPlayer(1);
        int num = rules.captureStones(1);

        /* return value should be 5 to include singleton
        stone on players side*/
        assertEquals(5,num);
       //pits 1 and 12 should be empty
       assertEquals(0,data.getNumStones(1));
       assertEquals(0,data.getNumStones(12)); 
       /*not testing the player's store as it is not 
       the responsibility of capture stones to put stones
       in the store*/
}
}