package com.lm2a.tacoonline.data;

import com.lm2a.tacoonline.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class JdbcIngredientRepository implements IngredientRepository {

    final JdbcTemplate jdbc;

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query("SELECT id, name, type FROM Ingredient", this::mapRowToIngredient);
    }

    @Override
    public Ingredient findOne(String id) {
        return jdbc.queryForObject("SELECT id, name, type FROM Ingredient WHERE id = ?", this::mapRowToIngredient, id);
    }

    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type")));
    }

}
