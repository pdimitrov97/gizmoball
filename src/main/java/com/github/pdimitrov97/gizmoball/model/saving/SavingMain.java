package com.github.pdimitrov97.gizmoball.model.saving;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Converter.toL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.swing.KeyStroke;

import com.github.pdimitrov97.gizmoball.model.Absorber;
import com.github.pdimitrov97.gizmoball.model.Ball;
import com.github.pdimitrov97.gizmoball.model.CircleGizmo;
import com.github.pdimitrov97.gizmoball.model.Gizmo;
import com.github.pdimitrov97.gizmoball.model.LeftFlipper;
import com.github.pdimitrov97.gizmoball.model.RightFlipper;
import com.github.pdimitrov97.gizmoball.model.Square;
import com.github.pdimitrov97.gizmoball.model.Triangle;
import com.github.pdimitrov97.gizmoball.physics.Vect;

public class SavingMain {
    private Map<Gizmo, String> variableNames = new HashMap<>();
    private Map<Ball, String> ballNames = new HashMap<>();

    private int varNo = 0;

    private String generateUniqueVarName(Gizmo g) {
        assert g != null : "Gizmo argument should not be null";

        final String varName = "var" + varNo++;
        variableNames.put(g, varName);
        return varName;
    }

    private String generateUniqueVarName(Ball b) {
        assert b != null : "Ball argument should not be null";

        final String varName = "var" + varNo++;
        ballNames.put(b, varName);
        return varName;
    }

    public List<String> convertGizmosToCommands(Collection<Gizmo> gizmos, Collection<Ball> balls, Map<KeyStroke, List<Gizmo>> keyConnections, double gravity, double mu, double mu2) {
        return new ArrayList<String>() {{
            addAll(getGizmoCreateCommands(gizmos));
            addAll(getRotateCommands(gizmos));
            addAll(getCreateBallCommands(balls));
            addAll(getGizmoConnectCommands(gizmos));
            addAll(getKeyConnectCommands(keyConnections));
            add("Gravity " + gravity);
            add("Friction " + mu + " " + mu2);
        }};
    }

    private List<String> getGizmoCreateCommands(Collection<Gizmo> gizmos) {
        List<String> commands = new ArrayList<>();

        for (Gizmo g : gizmos) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(getGizmoName(g));
            strBuilder.append(' ');

            strBuilder.append(generateUniqueVarName(g));
            strBuilder.append(' ');

            if (g instanceof Absorber) {
                Absorber a = (Absorber) g;
                strBuilder.append(a.getXL());
                strBuilder.append(' ');
                strBuilder.append(a.getYL());
                strBuilder.append(' ');
                strBuilder.append(a.getX2L());
                strBuilder.append(' ');
                strBuilder.append(a.getY2L());
                strBuilder.append(' ');
            } else if (g instanceof LeftFlipper || g instanceof RightFlipper) {
                strBuilder.append(toL(g.getX()));
                strBuilder.append(' ');
                strBuilder.append(toL(g.getY()));
                strBuilder.append(' ');
            } else {
                strBuilder.append(toL(g.getX()));
                strBuilder.append(' ');
                strBuilder.append(toL(g.getY()));
                strBuilder.append(' ');
            }
            commands.add(strBuilder.toString());
        }

        return commands;
    }

    private static String getGizmoName(Gizmo g) {
        if (g instanceof Square)
            return "Square";
        if (g instanceof CircleGizmo)
            return "Circle";
        if (g instanceof Triangle)
            return "Triangle";
        if (g instanceof RightFlipper)
            return "RightFlipper";
        if (g instanceof LeftFlipper)
            return "LeftFlipper";
        if (g instanceof Absorber)
            return "Absorber";

        throw new AssertionError("Unknown gizmo type");
    }

    private List<String> getRotateCommands(Collection<Gizmo> gizmos) {
        List<String> commands = new ArrayList<>();

        for (Gizmo g : gizmos) {
            String varName = variableNames.get(g);
            assert varName != null : "All varNames should be added already";


            if (g instanceof Triangle) {
                int rotateNo = ((Triangle) g).getNumberOfRotations();

                if (rotateNo != 0) {
                    for (int i = 0; i < rotateNo; i++)
                        commands.add("Rotate " + varName);
                }
            } else if (g instanceof LeftFlipper) {
                int rotateNo = ((LeftFlipper) g).getNumberOfRotations();

                if (rotateNo != 0) {
                    for (int i = 0; i < rotateNo; i++)
                        commands.add("Rotate " + varName);
                }
            } else if (g instanceof RightFlipper) {
                int rotateNo = ((RightFlipper) g).getNumberOfRotations();

                if (rotateNo != 0) {
                    for (int i = 0; i < rotateNo; i++)
                        commands.add("Rotate " + varName);
                }
            }
        }

        return commands;
    }

    private List<String> getGizmoConnectCommands(Collection<Gizmo> gizmos) {
        final List<String> commands = new ArrayList<>();

        gizmos.forEach(triggerProducer -> {
            triggerProducer.getConnectedGizmos().forEach(triggerConsumer -> {
//                if (triggerProducer != triggerConsumer)
                    commands.add("Connect " + variableNames.get(triggerProducer) + " " + variableNames.get(triggerConsumer));
            });
        });

        return commands;
    }

    private List<String> getKeyConnectCommands(final Map<KeyStroke, List<Gizmo>> keyConnections) {
        final BiFunction<KeyStroke, Gizmo, String> keyConnectToCommand = (keyStroke, gizmo) -> {
            final int keyNum = keyStroke.getKeyCode();
            final String keyUpDown = keyStroke.isOnKeyRelease() ? "up" : "down";
            final String varName = variableNames.get(gizmo);
            return "KeyConnect key " + keyNum + " " + keyUpDown + " " + varName;
        };

        return keyConnections.entrySet().parallelStream()
                .flatMap(mapEntry -> {
                    final KeyStroke keyStroke = mapEntry.getKey();
                    final List<Gizmo> gizmos = mapEntry.getValue();
                    return gizmos.parallelStream().map(gizmo -> keyConnectToCommand.apply(keyStroke, gizmo));
                })
                .collect(Collectors.toList());
    }

    private List<String> getCreateBallCommands(Collection<Ball> balls) {
        List<String> commands = new ArrayList<>();
        for (Ball b : balls) {
            if (b == null)
                continue;

            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("Ball ");

            strBuilder.append(generateUniqueVarName(b));
            strBuilder.append(' ');

            Vect center = b.getCircle().getCenter();
            strBuilder.append(center.x() / ONE_L_IN_PX);
            strBuilder.append(' ');

            strBuilder.append(center.y() / ONE_L_IN_PX);
            strBuilder.append(' ');

            Vect velocity = b.getVelo();
            strBuilder.append(velocity.x());
            strBuilder.append(' ');

            strBuilder.append(velocity.y());
            strBuilder.append(' ');

            commands.add(strBuilder.toString());
        }

        return commands;
    }
}
