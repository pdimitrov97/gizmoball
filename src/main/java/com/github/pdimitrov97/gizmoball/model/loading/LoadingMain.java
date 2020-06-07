package com.github.pdimitrov97.gizmoball.model.loading;

import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import javax.swing.KeyStroke;

import com.github.pdimitrov97.gizmoball.model.Absorber;
import com.github.pdimitrov97.gizmoball.model.Ball;
import com.github.pdimitrov97.gizmoball.model.CircleGizmo;
import com.github.pdimitrov97.gizmoball.model.Gizmo;
import com.github.pdimitrov97.gizmoball.model.LeftFlipper;
import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.model.RightFlipper;
import com.github.pdimitrov97.gizmoball.model.Square;
import com.github.pdimitrov97.gizmoball.model.Triangle;

public class LoadingMain {
    // These Maps map variable names to the appropriate object
    // AND their .values() collection is how other classes retrieves these objects
    private Map<String, Gizmo> gizmoVariables;
    private Map<String, Ball> ballVariables;

    // Error messages will be given to the client via this list
    private List<String> messages;
    // Model field must be provided so that its gravity and friction can be controlled
    private final Model model;

    private boolean compileError = false;
    private boolean readData = false;

    // The following map makes it neater to create the correct gizmo
    private static final Map<String, BiFunction<Integer, Integer, Gizmo>> gizmoMakers
            = Collections.unmodifiableMap(new HashMap<String, BiFunction<Integer, Integer, Gizmo>>() {{
        put("Square", Square::new);
        put("Circle", CircleGizmo::new);
        put("Triangle", Triangle::new);
        put("RightFlipper", RightFlipper::new);
        put("LeftFlipper", LeftFlipper::new);
    }});

    public LoadingMain(Model model) {
        this.model = model;
    }

    // Returns false if there is a problem with the commands in the file
    public boolean loadFile(final File fileName) throws IOException {
        gizmoVariables = new HashMap<>();
        ballVariables = new HashMap<>();
        messages = new ArrayList<>();
        compileError = false;

        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        // Prepare Regex Patterns
        // Basic Regex Patterns
        final String whiteSpace = "\\s+";
        final String optionalWhiteSpace = "\\s*";

        final String varNameRegex = whiteSpace + "\\S+";
        final String intRegex = "\\d+";
        final String intPair = whiteSpace + intRegex + whiteSpace + intRegex;
        final String floatRegex = "\\d+(.\\d+)?";
        final String floatPair = whiteSpace + floatRegex + whiteSpace + floatRegex;

        final String plusMinusFloatRegex = "-?\\d+(.\\d+)?";
        final String plusMinusFloatPair = whiteSpace + plusMinusFloatRegex + whiteSpace + plusMinusFloatRegex;

        // Command Regex Patterns
        final Pattern addGizmo = Pattern.compile("(Square|Circle|Triangle|LeftFlipper|RightFlipper)" + varNameRegex + intPair + optionalWhiteSpace);
        final Pattern addAbsorber = Pattern.compile("Absorber" + varNameRegex + intPair + intPair + optionalWhiteSpace);
        final Pattern addBall = Pattern.compile("Ball" + varNameRegex + floatPair + plusMinusFloatPair + optionalWhiteSpace);
        final Pattern rotate = Pattern.compile("Rotate" + varNameRegex + optionalWhiteSpace);
        final Pattern delete = Pattern.compile("Delete" + varNameRegex + optionalWhiteSpace);
        final Pattern moveIntPair = Pattern.compile("Move" + varNameRegex + intPair + optionalWhiteSpace);
        final Pattern moveFloatPair = Pattern.compile("Move" + varNameRegex + floatPair + optionalWhiteSpace);
        final Pattern gizmoConnect = Pattern.compile("Connect" + varNameRegex + varNameRegex);
        final Pattern keyConnect = Pattern.compile("KeyConnect" + whiteSpace + "key" + whiteSpace + intRegex + whiteSpace + "(up|down)" + varNameRegex + optionalWhiteSpace);
        final Pattern gravity = Pattern.compile("Gravity" + whiteSpace + floatRegex + optionalWhiteSpace);
        final Pattern friction = Pattern.compile("Friction" + floatPair + optionalWhiteSpace);

        // Match commands
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.equals(""))
                continue;

            String[] args = line.split(whiteSpace);
            if (args.length < 2) {
                messages.add("Incorrect command: " + line);
                continue;
            }

            final String varName = args[1];

            if (addGizmo.matcher(line).matches()) {
                if (doesVariableExist(varName)) {
                    messages.add(varName + " is being created more than once!");
                    continue;
                }
                final int x = Integer.valueOf(args[2]);
                final int y = Integer.valueOf(args[3]);

                final Gizmo toAdd = gizmoMakers.get(args[0]).apply(x, y);
                gizmoVariables.put(varName, toAdd);
            } else if (addAbsorber.matcher(line).matches()) {
                if (doesVariableExist(varName)) {
                    messages.add(varName + " is being created more than once!");
                    continue;
                }

                final int x1 = Integer.valueOf(args[2]);
                final int y1 = Integer.valueOf(args[3]);
                final int x2 = Integer.valueOf(args[4]);
                final int y2 = Integer.valueOf(args[5]);

                final Gizmo toAdd = new Absorber(x1, y1, x2, y2);
                gizmoVariables.put(varName, toAdd);
            } else if (addBall.matcher(line).matches()) {
                if (doesVariableExist(varName)) {
                    messages.add(varName + " is being created more than once!");
                    continue;
                }

                final double x = Double.valueOf(args[2]);
                final double y = Double.valueOf(args[3]);
                final double xv = Double.valueOf(args[4]);
                final double yv = Double.valueOf(args[5]);

                final Ball toAdd = new Ball(toPx(x), toPx(y), xv, yv);
                ballVariables.put(varName, toAdd);
            } else if (rotate.matcher(line).matches()) {
                if (!gizmoVariables.containsKey(varName)) {
                    messages.add(varName + " has not been created and so it cannot be rotated");
                    continue;
                }

                final Gizmo toRotate = gizmoVariables.get(varName);
                if (toRotate instanceof Triangle)
                    ((Triangle) toRotate).rotate();
                else if (toRotate instanceof LeftFlipper)
                    ((LeftFlipper) toRotate).rotate();
                else if (toRotate instanceof RightFlipper)
                    ((RightFlipper) toRotate).rotate();
                else
                    messages.add("Cannot rotate " + toRotate.getClass().getName() + " " + varName);
            } else if (delete.matcher(line).matches()) {
                if (!gizmoVariables.containsKey(varName)) {
                    messages.add(varName + " has not been created and so it cannot be deleted");
                    continue;
                }

                gizmoVariables.remove(varName);
            } else if (moveIntPair.matcher(line).matches() || moveFloatPair.matcher(line).matches()) {
                if(ballVariables.containsKey(varName)) {
                    final Ball toMove = ballVariables.get(varName);
                    final double x = Double.valueOf(args[2]);
                    final double y = Double.valueOf(args[3]);
                    toMove.setExactX(toPx(x));
                    toMove.setExactY(toPx(y));
                } else if(gizmoVariables.containsKey(varName)) {
                    if(!moveIntPair.matcher(line).matches()) {
                        messages.add(line + " is not valid for moving a gizmo");
                        continue;
                    }

                    final Gizmo toMove = gizmoVariables.get(varName);
                    final int x = Integer.valueOf(args[2]);
                    final int y = Integer.valueOf(args[3]);
                    toMove.move(x, y);
                } else {
                    messages.add(varName + " has not been created and so it cannot be moved");
                }
            } else if (gizmoConnect.matcher(line).matches()) {
                if (!gizmoVariables.containsKey(args[1])) {
                    messages.add(args[1] + " has not been created and so there is nothing to connect to");
                    continue;
                }
                if (!gizmoVariables.containsKey(args[2])) {
                    messages.add(args[2] + " has not been created and so it cannot be connected to");
                    continue;
                }

                final Gizmo triggerProducer = gizmoVariables.get(args[1]);
                final Gizmo triggerConsumer = gizmoVariables.get(args[2]);
                triggerProducer.connectGizmo(triggerConsumer);
            } else if (keyConnect.matcher(line).matches()) {
                if (!gizmoVariables.containsKey(args[4])) {
                    messages.add(varName + " has not been created and so it cannot be keyconnected");
                    continue;
                }

                final boolean isKeyRelease;
                switch (args[3]) {
                    case "up":
                        isKeyRelease = true;
                        break;
                    case "down":
                        isKeyRelease = false;
                        break;
                    default:
                        throw new AssertionError("Pattern matcher should've guaranteed that \"" + args[3] + "\" is either up or down");
                }

                final int asciiCode = Integer.valueOf(args[2]);
                final int keyCode = KeyEvent.getExtendedKeyCodeForChar(asciiCode);

                final KeyStroke triggerProducer = KeyStroke.getKeyStroke(keyCode, 0, isKeyRelease);
                final Gizmo triggerConsumer = gizmoVariables.get(args[4]);
                model.addKeyConnection(triggerProducer, triggerConsumer);
            } else if (gravity.matcher(line).matches()) {
                final double gravityValue = Double.valueOf(args[1]);
                model.setGravity((int) gravityValue);
            } else if (friction.matcher(line).matches()) {
                final double mu = Double.valueOf(args[1]);
                final double mu2 = Double.valueOf(args[2]);
                model.setFrictionMU(mu);
                model.setFrictionMU2(mu2);
            } else {
                messages.add("Incorrect command: " + line);
            }
        }

        reader.close();

        readData = true;
        compileError = !messages.isEmpty();
        return !compileError;
    }

    public boolean wasCompilationSuccessful() {
        return !compileError;
    }

    public List<Gizmo> getGizmos() {
        if (!readData)
            throw new IllegalStateException("Must call loadFile() before getGizmos()");

        return new ArrayList<>(gizmoVariables.values());
    }

    public List<Ball> getBalls() {
        if (!readData)
            throw new IllegalStateException("Must call readData() before getGizmos()");

        return new ArrayList<>(ballVariables.values());
    }

    public List<String> getMessages() {
        if (readData)
            return messages;
        else
            throw new IllegalStateException("Must call readData() before getMessages()");
    }

    private boolean doesVariableExist(String varName) {
        return gizmoVariables.containsKey(varName) || ballVariables.containsKey(varName);
    }
}