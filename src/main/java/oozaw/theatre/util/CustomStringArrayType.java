package oozaw.theatre.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;

public class CustomStringArrayType implements UserType<String[]> {

   @Override
   public int getSqlType() {
      return Types.ARRAY;
   }

   @Override
   public Class returnedClass() {
      return String[].class;
   }

   @Override
   public boolean equals(String[] x, String[] y) {
      if (x == null) {
         return y == null;
      }

      return Arrays.equals(x, y);
   }

   @Override
   public int hashCode(String[] strings) {
      return Arrays.hashCode(strings);
   }

   @Override
   public String[] nullSafeGet(ResultSet resultSet, int i, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws SQLException {
      Array array = resultSet.getArray(i);
      return array != null ? (String[]) array.getArray() : null;
   }

   @Override
   public void nullSafeSet(PreparedStatement preparedStatement, String[] strings, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws SQLException {
      if (preparedStatement != null) {
         if (strings != null) {
            Array array = sharedSessionContractImplementor.getJdbcConnectionAccess().obtainConnection()
               .createArrayOf("text", strings);
            preparedStatement.setArray(i, array);
         } else {
            preparedStatement.setNull(i, Types.ARRAY);
         }
      }
   }

   @Override
   public String[] deepCopy(String[] strings) {
      return strings;
   }

   @Override
   public boolean isMutable() {
      return true;
   }

   @Override
   public Serializable disassemble(String[] strings) {
      return this.deepCopy(strings);
   }

   @Override
   public String[] assemble(Serializable serializable, Object o) {
      return new String[0];
   }

   @Override
   public String[] replace(String[] detached, String[] managed, Object owner) {
      return UserType.super.replace(detached, managed, owner);
   }
}
