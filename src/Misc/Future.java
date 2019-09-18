package Misc;

public class Future {

//  public static String[][] getData(int id) {
//    openConnection();
//    String result[][] = new String[0][0];
//      try {
//        PreparedStatement st = conn.prepareStatement(superSelect());
//         
//        st = conn.prepareStatement(superSelect());
//        st.setInt(1, id);
//        st.setInt(2, id);
//        ResultSet rs = st.executeQuery();
//        rs.last();
//        int rsRowCount = rs.getRow(); 
//        int rsColumnCount = rs.getMetaData().getColumnCount();
//        rs.beforeFirst();
//
//        String aux[][] = new String[rsRowCount][rsColumnCount];
//          for (int i = 0; i < rsRowCount; i++) {
//            rs.next();
//            aux[i][0] = String.valueOf(rs.getInt("LASTOWNER"));
//            aux[i][1] = rs.getString("LASTMESSAGE");
//          }
//        result = aux;
//      }
//      catch (SQLException e) {
//        System.out.println("SQLException in getData(id)" + superSelect());
//      }
//      finally{
//        closeConnection();
//        return result;
//      }
//  }
  
//  public static String superSelect () {    
//    return "SELECT " +    
//      "(SELECT MESSAGE " +
//       "FROM MESSAGES A " +
//       "WHERE (A.OWNER = B.OWNER " +
//              "OR A.RECEIVER = B.OWNER) " +
//         "AND (A.OWNER = B.RECEIVER " +
//              "OR A.RECEIVER = B.RECEIVER) " +
//       "ORDER BY DATAHORA DESC " +
//       "LIMIT 1) LASTMESSAGE, " +
//       "(SELECT CASE " + 
//                  "WHEN RECEIVER = ? THEN OWNER " +
//                  "ELSE RECEIVER " +
//       "FROM MESSAGES A " +
//       "WHERE (A.OWNER = B.OWNER " +
//              "OR A.RECEIVER = B.OWNER) " +
//         "AND (A.OWNER = B.RECEIVER " +
//              "OR A.RECEIVER = B.RECEIVER) " +
//       "ORDER BY DATAHORA DESC " +
//       "LIMIT 1) LASTOWNER " +
//    "FROM  MESSAGES B WHERE OWNER = ?";
//  }

  
}
