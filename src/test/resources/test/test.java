import io.github.Hattinger04.hamsterEvaluation.debugger.model.Territorium;
import io.github.Hattinger04.hamsterEvaluation.debugger.model.Territory;
import io.github.Hattinger04.hamsterEvaluation.model.HamsterException;
import io.github.Hattinger04.hamsterEvaluation.model.HamsterInitialisierungsException;
import io.github.Hattinger04.hamsterEvaluation.model.HamsterNichtInitialisiertException;
import io.github.Hattinger04.hamsterEvaluation.model.KachelLeerException;
import io.github.Hattinger04.hamsterEvaluation.model.MauerDaException;
import io.github.Hattinger04.hamsterEvaluation.model.MaulLeerException;
import io.github.Hattinger04.hamsterEvaluation.model.MouthEmptyException;
import io.github.Hattinger04.hamsterEvaluation.model.WallInFrontException;
import io.github.Hattinger04.hamsterEvaluation.model.TileEmptyException;

public class test extends io.github.Hattinger04.hamsterEvaluation.debugger.model.IHamster implements io.github.Hattinger04.hamsterEvaluation.model.HamsterProgram {
    public void main() {
        linksUm();
        linksUm();
        vor();
    }
}