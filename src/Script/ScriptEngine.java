package Script;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScriptEngine {

    private final Map<String, ScriptStep> steps = new HashMap<>();
    private final Set<String> usedSteps = new HashSet<>();

    public void addStep(String from, String to) {
        steps.put(from, new ScriptStep(from, to));
    }

    public boolean canRunScript(String roomId) {
        return steps.containsKey(roomId) && !usedSteps.contains(roomId);
    }

    public String runScript(String roomId) {
        ScriptStep step = steps.get(roomId);
        if (step == null) return null;
        usedSteps.add(roomId);
        return step.getToRoom();
    }
}
