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

@Repository
public class TacoRepoImpl implements TacoRepo {


    JdbcTemplate jdbc;

    public TacoRepoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco design) {
        long tacoId = saveTacoInfo(design);
        design.setId(tacoId);
        for (Ingredient ingredient : design.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }
        return design;
    }

    private long saveTacoInfo(Taco design) {
        design.setCreatedAt(new Date());
        PreparedStatementCreatorFactory preparedFactory = new PreparedStatementCreatorFactory("INSERT INTO taco (name, createdAt) VALUES (?,?)", Types.VARCHAR, Types.TIMESTAMP);
        preparedFactory.setReturnGeneratedKeys(true);

        PreparedStatementCreator preparedCreator = preparedFactory.newPreparedStatementCreator(Arrays.asList(design.getName(), new Timestamp(design.getCreatedAt().getTime())));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(preparedCreator, keyHolder);
        Number key = keyHolder.getKey();
        return key.longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbc.update("INSERT INTO Taco_Ingredients (taco_id, ingredient_id) VALUES (?,?)", tacoId, ingredient.getId());
    }


}
