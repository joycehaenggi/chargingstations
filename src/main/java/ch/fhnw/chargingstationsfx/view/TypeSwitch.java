package ch.fhnw.chargingstationsfx.view;


import javafx.animation.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * Toggle switch für loaderType
 *
 * @author Sandro Osswald
 * @author Cedric Bühler
 */

public class TypeSwitch extends Region {
    private static final double ARTBOARD_WIDTH  = 450;
    private static final double ARTBOARD_HEIGHT = 50;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 100;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 600;

    private static final Color BUTTON_ON = Color.rgb(200, 0 , 0);
    private static final Color BUTTON_OFF = Color.rgb(255, 208, 0);

    private static final String fast = "Schnellladeeinrichtung";
    private static final String normal = "Normalladeeinrichtung";

    private Color barColor1 = Color.rgb(255, 220, 0, 0.7);
    private Color barColor2 = Color.rgb(250, 250, 0, 0.6);
    private Color barColor3 = Color.rgb(250, 250, 0, 0.4);
    private Color textColor = Color.rgb(255, 255, 255);

    private Timeline timeline = new Timeline();
    private Timeline timeline2 = new Timeline();

    private Rectangle button;
    private Rectangle bar;
    private Text      text;

    private int speedValue = 25;

    private double widthOfOneGradientCycle = 20.0;
    private double gradientSlopeDegree = 45.0;
    private double xStartStatic = 100.0;
    private double yStartStatic = 100.0;
    private double xEndStatic = xStartStatic + (widthOfOneGradientCycle * Math.cos(Math.toRadians(gradientSlopeDegree)));
    private double yEndStatic = yStartStatic + (widthOfOneGradientCycle * Math.sin(Math.toRadians(gradientSlopeDegree)));

    private StringProperty type = new SimpleStringProperty();


    // needed for resizing
    private Pane drawingPane;

    //public TypeSwitch(RootPM pm) {
    public TypeSwitch() {
        initializeSelf();
        initializeParts();
        initializeDrawingPane();
        initializeAnimations();
        layoutParts();
        setupEventHandlers();
        setupValueChangeListeners();
        setupBinding();
    }

    private void initializeSelf() {
        String stylesheet = getClass().getResource("typeSwitch.css").toExternalForm();
        getStylesheets().add(stylesheet);

        getStyleClass().add("toggleSwitch");
    }

    private void initializeParts() {

        bar = new Rectangle(0, 0, ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        bar.setMouseTransparent(true);
        bar.getStyleClass().addAll(("bar"));

        button = new Rectangle(0, 0, ARTBOARD_WIDTH/3.5, ARTBOARD_HEIGHT);
        button.getStyleClass().addAll("button");
        button.setStrokeWidth(0);

        text = new Text(0, (ARTBOARD_HEIGHT/2)+10, "exampletext");
        text.getStyleClass().addAll("display");
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void initializeAnimations(){

            }

    private void layoutParts() {
        drawingPane.getChildren().addAll(bar, button, text);

        getChildren().add(drawingPane);
    }


    private void setupEventHandlers() {
        drawingPane.setOnMouseClicked(event -> {
            if(getType().equals(fast)){
                setType(normal);
            }else{
                setType(fast);
            }
        });

    }

    private void setupValueChangeListeners() {
        typeProperty().addListener((observable, oldValue, newValue) -> {


            if(timeline.getStatus().equals(Animation.Status.RUNNING)){
                return;
            }
            KeyValue buttonMoveValue;
            KeyValue buttonColorValue;
            KeyValue textMoveValue;
            KeyValue textColorValue;

            if(typeProperty().getValue().equals(normal)) {

                barColor1 = Color.rgb(255, 220, 0, 0.7);
                barColor2 = Color.rgb(250, 250, 0, 0.6);
                barColor3 = Color.rgb(250, 250, 0, 0.4);
                textColor = Color.rgb(50, 50, 70);
                buttonMoveValue = new KeyValue(button.xProperty(), 0);
                buttonColorValue = new KeyValue(button.fillProperty(), BUTTON_OFF);
                textColorValue = new KeyValue(text.fillProperty(), textColor);
                textMoveValue = new KeyValue(text.xProperty(), (ARTBOARD_WIDTH/3.5)+10);

                setSpeedValue(40);


            }else {
                barColor1 = Color.rgb(255, 0, 0, 0.7);
                barColor2 = Color.rgb(250, 0, 0, 0.6);
                barColor3 = Color.rgb(250, 50, 0, 0.6);
                textColor = Color.rgb(255, 255, 255);
                buttonMoveValue = new KeyValue(button.xProperty(), ARTBOARD_WIDTH -(ARTBOARD_WIDTH/3.5));
                buttonColorValue = new KeyValue(button.fillProperty(), BUTTON_ON);
                textColorValue = new KeyValue(text.fillProperty(), textColor);
                textMoveValue = new KeyValue(text.xProperty(), 10);

                setSpeedValue(25);
            }

            KeyFrame frame = new KeyFrame(Duration.seconds(0.5), buttonMoveValue, buttonColorValue, textMoveValue,textColorValue); //barColorValue

            timeline.getKeyFrames().setAll(frame);

            timeline.setCycleCount(1);

            timeline.setAutoReverse(false);

            timeline.play();

            timeline2.stop();
            timeline2.getKeyFrames().clear();

            for (int i = 0; i < 10; i++) {
                int innerIterator = i;
                KeyFrame kf = new KeyFrame(Duration.millis(speedValue * innerIterator), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent ae) {

                        double runningRadius = innerIterator * (widthOfOneGradientCycle / 10);
                        double xStartDynamic = xStartStatic + (runningRadius * Math.cos(Math.toRadians(gradientSlopeDegree)));
                        double yStartDynamic = yStartStatic + (runningRadius * Math.sin(Math.toRadians(gradientSlopeDegree)));
                        double xEndDynamic = xEndStatic + (runningRadius * Math.cos(Math.toRadians(gradientSlopeDegree)));
                        double yEndDynamic = yEndStatic + (runningRadius * Math.sin(Math.toRadians(gradientSlopeDegree)));

                        LinearGradient gradient = new LinearGradient(xStartDynamic, yStartDynamic, xEndDynamic, yEndDynamic,
                                false, CycleMethod.REPEAT, new Stop[]{

                                new Stop(0.5, barColor1),
                                new Stop(1.0, barColor2),
                                new Stop(0.0, barColor3)
                        });
                        bar.setFill(gradient);
                    }
                });
                timeline2.getKeyFrames().add(kf);
                timeline2.setCycleCount(Timeline.INDEFINITE);

            }

            timeline2.play();

        });
    }

    private void setupBinding() {
        text.textProperty().bind(type);
    }


    //resize by scaling
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void resize() {
        Insets padding         = getPadding();
        double availableWidth  = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    // some handy functions

    private double percentageToValue(double percentage, double minValue, double maxValue){
        return ((maxValue - minValue) * percentage) + minValue;
    }

    private double valueToPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue);
    }

    private double valueToAngle(double value, double minValue, double maxValue) {
        return percentageToAngle(valueToPercentage(value, minValue, maxValue));
    }

    private double mousePositionToValue(double mouseX, double mouseY, double cx, double cy, double minValue, double maxValue){
        double percentage = angleToPercentage(angle(cx, cy, mouseX, mouseY));

        return percentageToValue(percentage, minValue, maxValue);
    }

    private double angleToPercentage(double angle){
        return angle / 360.0;
    }

    private double percentageToAngle(double percentage){
        return 360.0 * percentage;
    }

    private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx     = deltaX / radius;
        double ny     = deltaY / radius;
        double theta  = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }

    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
                cY + (radius * Math.cos(Math.toRadians(angle - 180))));
    }

    private Text createCenteredText(String styleClass) {
        return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
    }

    private Text createCenteredText(double cx, double cy, String styleClass) {
        Text text = new Text();
        text.getStyleClass().add(styleClass);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
        text.setWrappingWidth(width);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setY(cy);
        text.setX(cx - (width / 2.0));

        return text;
    }

    private Text leftAndRightText(String styleClass) {
        return leftAndRightText(10, 0, styleClass);
    }

    private Text leftAndRightText(double cx, double cy, String styleClass) {
        Text text = new Text();
        text.getStyleClass().add(styleClass);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.LEFT);
        text.setY(cy + 150 + 50);
        if (typeProperty().get() == fast) {
            text.setX(cx);
        } else {
            text.setX(cx + 150);
        }

        return text;
    }

    private Group createTicks(double cx, double cy, int numberOfTicks, double overallAngle, double tickLength, double indent, double startingAngle, String styleClass) {
        Group group = new Group();

        double degreesBetweenTicks = overallAngle == 360 ?
                overallAngle /numberOfTicks :
                overallAngle /(numberOfTicks - 1);
        double outerRadius         = Math.min(cx, cy) - indent;
        double innerRadius         = Math.min(cx, cy) - indent - tickLength;

        for (int i = 0; i < numberOfTicks; i++) {
            double angle = 180 + startingAngle + i * degreesBetweenTicks;

            Point2D startPoint = pointOnCircle(cx, cy, outerRadius, angle);
            Point2D endPoint   = pointOnCircle(cx, cy, innerRadius, angle);

            Line tick = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
            tick.getStyleClass().add(styleClass);
            group.getChildren().add(tick);
        }

        return group;
    }

    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // alle getter und setter  (generiert via "Code -> Generate... -> Getter and Setter)



    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getSpeedValue() {
        return speedValue;
    }

    public void setSpeedValue(int speedValue) {
        this.speedValue = speedValue;
    }
}
