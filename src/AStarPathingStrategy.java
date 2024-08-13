import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> calculatedPath = new LinkedList<>();
        List<Point> visited = new LinkedList<>();
        AStarNode startNode = new AStarNode(start, new LinkedList<>(Arrays.asList(start)), 0);
        List<AStarNode> openList = new LinkedList<>();
        openList.add(startNode);

        while (!openList.isEmpty()) {
            AStarNode current = openList.stream()
                    .min(Comparator.comparingInt(AStarNode::getF))
                    .orElseThrow(() -> new IllegalArgumentException("Node list is empty"));

            openList.remove(current);
            if (withinReach.test(current.getPoint(), end)) {
                calculatedPath = current.getPath();
                break;
            } else {
                visited.add(current.getPoint());
                List<Point> filteredNeighbors = potentialNeighbors.apply(current.getPoint())
                        .filter(canPassThrough)
                        .filter(pt -> !visited.contains(pt))
                        .toList();

                for (Point neighbor : filteredNeighbors) {
                    int gScore = current.getPath().size();
                    int h = (int) Math.round(neighbor.distance(end));
                    int fScore = gScore + h;
                    List<Point> newPath = new LinkedList<>(current.getPath());
                    newPath.add(neighbor);
                    AStarNode newNode = new AStarNode(neighbor, newPath, fScore);
                    addTo(openList, newNode);
                }
            }
        }

        if (!calculatedPath.isEmpty()) {
            calculatedPath.remove(0);
        }

        return calculatedPath;
    }

    private void addTo(List<AStarNode> openList, AStarNode node) {
        boolean ifPtAdded = false;
        for (int i = 0; i < openList.size(); i++) {
            AStarNode currNode = openList.get(i);
            if (currNode.getPoint().equals(node.getPoint())) {
                if (node.getF() < currNode.getF()) {
                    openList.set(i, node);
                }
                ifPtAdded = true;
                break;
            } else if (node.getF() < currNode.getF()) {
                openList.add(i, node);
                ifPtAdded = true;
                break;
            }
        }
        if (ifPtAdded == false) {
            openList.add(node);
        }
    }

}
