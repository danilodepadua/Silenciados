package com.proj.main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class SimpleGameApp extends GameApplication {

    private Entity player;
    private final int speed = 100;
    private Vec2 MOVE = new Vec2();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Simple Game with FXGL");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        PhysicsComponent Fisica = new PhysicsComponent();
        Fisica.setBodyType(BodyType.DYNAMIC);
        Fisica.setOnPhysicsInitialized(() -> Fisica.getBody().setGravityScale(0));
        FixtureDef fd = new FixtureDef();
        fd.setDensity(0.7f);
        fd.setRestitution(0.3f);
        Fisica.setFixtureDef(fd);

        PhysicsComponent FisicaInimigo = new PhysicsComponent();
        FixtureDef fdd = new FixtureDef();
        fdd.setDensity(0.7f);
        fdd.setRestitution(0.3f);
        FisicaInimigo.setFixtureDef(fdd);
        player = entityBuilder()
                .type(EntityTypes.Player)
                .at(100, 100)
                .viewWithBBox(new Rectangle(40, 40, Color.BLUE))
                .collidable()
                .with(Fisica)
                .buildAndAttach();
        Entity enimy = entityBuilder()
                .type(EntityTypes.Inimigo)
                .at(100, 300)
                .viewWithBBox(new Rectangle(50, 50, Color.RED))
                .collidable()
                .with(FisicaInimigo)
                .buildAndAttach();

        getGameScene().getViewport().setBounds(-500,-500,500, 500);
        getGameScene().getViewport().bindToEntity(player,400, 300);
    }

    @Override
    protected void initPhysics() {

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityTypes.Player, EntityTypes.Inimigo) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
                System.out.println("Colidiu");
                //b.removeFromWorld();
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        player.getComponent(PhysicsComponent.class).setLinearVelocity(MOVE.x*speed, MOVE.y*speed);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                MOVE.x = 1; // Move 5 pixels to the right
            }

            @Override
            protected void onActionEnd() {
                MOVE.x = 0; // Move 5 pixels to the right
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                MOVE.x = -1; // Move 5 pixels to the right
            }

            @Override
            protected void onActionEnd() {
                MOVE.x = 0; // Move 5 pixels to the right
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                MOVE.y = -1; // Move 5 pixels to the right
            }
            @Override
            protected void onActionEnd() {
                MOVE.y = 0; // Move 5 pixels to the right
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                MOVE.y = 1; // Move 5 pixels to the right
            }
            @Override
            protected void onActionEnd() {
                MOVE.y = 0; // Move 5 pixels to the right
            }
        }, KeyCode.S);
    }

    public static void main(String[] args) {
        launch(args);
    }
}