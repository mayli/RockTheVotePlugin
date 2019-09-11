package rtv;

import java.util.HashSet;

import io.anuke.arc.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.game.Team;
import io.anuke.mindustry.game.EventType.*;
import io.anuke.mindustry.gen.*;
import io.anuke.mindustry.plugin.Plugin;
import io.anuke.mindustry.Vars;

public class RockTheVotePlugin extends Plugin {

    static private double ratio = 0.6;
    private HashSet<Player> votes = new HashSet<>();
    private boolean enable = true;

    // register event handlers and create variables in the constructor
    public RockTheVotePlugin() {
    }

    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler){

        //register a simple reply command
        handler.<Player>register("rtv", "[off]", "Rock the vote to change map", (args, player) -> {
            if (player.isAdmin){
                if (args.length == 1 && args[0].equals("off")) {
                    this.enable = false;
                } else {
                    this.enable = true;
                }
            }
            if (!this.enable) {
                player.sendMessage("RTV: RockTheVote is disabled");
                return;
            }
            this.votes.add(player);
            int cur = this.votes.size();
            int req = (int) Math.ceil(ratio * Vars.playerGroup.size());
            Call.sendMessage("RTV: [accent]" + player.name + "[] wants to change the map, [green]" + cur +
                "[] votes, [green]" + req + "[] required");

            if (cur < req) {
                return;
            }

            this.votes.clear();
            Call.sendMessage("RTV: [green] vote passed, changing map.");
            Events.fire(new GameOverEvent(Team.crux));
        });
    }
}
