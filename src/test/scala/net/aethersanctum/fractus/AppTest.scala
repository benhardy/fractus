package net.aethersanctum.fractus;

import org.junit.Test
import net.aethersanctum.fractus.Transform._



/**
 * Unit test for simple App.
 */
class AppTest {

    @Test    
    def testBidness = {
      Transform rotate 5 scale 10
      val rules = rotate(5).scale(10) :: translate(10,10) :: Nil
      rules.foreach { println }
    }
}
