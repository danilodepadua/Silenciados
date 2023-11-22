package com.mygdx.game.Entities.Player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Components.GraphicsComponent;
import com.mygdx.game.Entities.Entity;
import com.mygdx.game.Entities.EntityConfig;
import com.mygdx.game.Mapa.MapManager;

// Componente gráfico para o jogador
public class PlayerGraphicsComponent extends GraphicsComponent {

    // Posição anterior do jogador
    protected Vector2 previousPosition;

    // Construtor padrão inicializando a posição anterior
    public PlayerGraphicsComponent() {
        previousPosition = new Vector2(0, 0);
    }

    // Método para receber mensagens e executar ações correspondentes
    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(MESSAGE_TOKEN);

        if (string.length == 0) {
            return;
        }

        if (string.length == 1) {
            if (string[0].equalsIgnoreCase(MESSAGE.RESET_POSITION.toString())) {
                currentPosition = null;
            }
        }

        // Especificamente para mensagens com carga útil de 1 objeto
        if (string.length == 2) {
            if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString())) {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                currentPosition = json.fromJson(Vector2.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                currentState = json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.LOAD_ANIMATIONS.toString())) {
                EntityConfig entityConfig = json.fromJson(EntityConfig.class, string[1]);
                Array<EntityConfig.AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

                for (EntityConfig.AnimationConfig animationConfig : animationConfigs) {
                    Array<String> textureNames = animationConfig.getTexturePaths();
                    Array<GridPoint2> points = animationConfig.getGridPoints();
                    Entity.AnimationType animationType = animationConfig.getAnimationType();
                    float frameDuration = animationConfig.getFrameDuration();
                    Animation<TextureRegion> animation = null;

                    if (textureNames.size == 1) {
                        animation = loadAnimation(textureNames.get(0), points, frameDuration);
                    } else if (textureNames.size == 2) {
                        animation = loadAnimation(textureNames.get(0), textureNames.get(1), points, frameDuration);
                    }

                    animations.put(animationType, animation);
                }
            }
        }
    }

    // Método para atualizar o componente gráfico do jogador
    @Override
    public void update(Entity entity, MapManager mapMgr, Batch batch, float delta) {
        updateAnimations(delta);

        // O jogador se moveu
        if (previousPosition.x != currentPosition.x || previousPosition.y != currentPosition.y) {
            previousPosition = currentPosition.cpy();
        }

        Camera camera = mapMgr.getCamera();
        camera.position.set(currentPosition.x, currentPosition.y, 0f);
        camera.update();

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();

        // Usado para debug gráfico de bounding boxes
        /*
        Rectangle rect = entity.getCurrentBoundingBox();
        _shapeRenderer.setProjectionMatrix(camera.combined);
        _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.RED);
        _shapeRenderer.rect(rect.getX() * Map.UNIT_SCALE , rect.getY() * Map.UNIT_SCALE, rect.getWidth() * Map.UNIT_SCALE, rect.getHeight()*Map.UNIT_SCALE);
        _shapeRenderer.end();
        */
    }

    // Método para liberar recursos
    @Override
    public void dispose() {
        // Nada
    }
}