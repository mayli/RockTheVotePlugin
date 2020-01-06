package rtv;

import java.util.HashSet;

import arc.*;
import arc.util.*;
import mindustry.entities.type.*;
import mindustry.game.Team;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.plugin.Plugin;
import mindustry.Vars;

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
