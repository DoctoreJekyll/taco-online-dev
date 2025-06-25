package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Ingredient;
import com.lm2a.tacoonline.model.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository // Marca esta clase como un componente Spring responsable de acceder a datos
public class TacoRepoImpl implements TacoRepo {

    JdbcTemplate jdbc; // Objeto de utilidad para ejecutar SQL desde Spring

    // Inyectamos el JdbcTemplate a través del constructor
    public TacoRepoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Guarda un taco en la base de datos, incluyendo sus ingredientes
    @Override
    public Taco save(Taco design) {
        // Guarda primero la información básica del taco y obtiene el ID generado
        long tacoId = saveTacoInfo(design);
        design.setId(tacoId); // Asigna el ID al objeto taco

        // Por cada ingrediente asociado al taco, inserta la relación en la tabla intermedia
        for (Ingredient ingredient : design.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }

        return design; // Devuelve el taco con ID asignado
    }

    // Guarda el nombre y la fecha de creación del taco en la tabla `taco`
    private long saveTacoInfo(Taco design) {
        design.setCreatedAt(new Date()); // Asigna la fecha de creación actual

        // Creamos una fábrica para crear statements parametrizados
        PreparedStatementCreatorFactory preparedFactory = new PreparedStatementCreatorFactory(
                "INSERT INTO taco (name, createdAt) VALUES (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP // Tipos de los parámetros
        );
        preparedFactory.setReturnGeneratedKeys(true); // Indicamos que queremos recuperar la clave generada

        // Creamos el statement con los valores actuales
        PreparedStatementCreator preparedCreator = preparedFactory.newPreparedStatementCreator(
                Arrays.asList(design.getName(), new Timestamp(design.getCreatedAt().getTime()))
        );

        KeyHolder keyHolder = new GeneratedKeyHolder(); // Almacena el ID generado
        jdbc.update(preparedCreator, keyHolder); // Ejecuta la inserción

        // Devuelve el ID generado como tipo long
        Number key = keyHolder.getKey();
        return key.longValue();
    }

    // Guarda la relación entre un taco y un ingrediente en la tabla intermedia `Taco_Ingredients`
    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbc.update(
                "INSERT INTO Taco_Ingredients (taco_id, ingredient_id) VALUES (?, ?)",
                tacoId, ingredient.getId()
        );
    }
}
