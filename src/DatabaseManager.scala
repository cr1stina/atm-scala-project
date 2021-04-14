import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.Date

class DatabaseManager(var url : String, var username : String, var password : String) {
  private var connection : Connection = _
  try{
    connection = DriverManager.getConnection(url, username, password)
    println("DATABASE SUCCESSFULLY CONNECTED")
  }catch{
    case e: Exception => e.printStackTrace()
  }

  private val statement = connection.createStatement()

  def login(id : String, pin : String) : Boolean = {
    val rs = statement.executeQuery(s"SELECT nameUser, lastName FROM user WHERE iduser = $id AND pin = $pin")
    if(rs.next())
      true
    else
      false
  }

  def getBankName(id : Int) : String = {
    val rs = statement.executeQuery(s"SELECT nameB FROM bank WHERE idBank = $id")
    if(rs.next())
      rs.getString("nameB")
    else
      null
  }

  def getUserName(id : String) : String = {
    val rs = statement.executeQuery(s"SELECT nameUser FROM user WHERE iduser = $id")
    if(rs.next()) {
      rs.getString("nameUser")
    } else
        null
  }

  def getAcctBalance(id : String) : Float ={
    val rs = statement.executeQuery(s"SELECT balance FROM account WHERE idAcct = $id")
    if(rs.next()){
      rs.getFloat("balance")
    }
    else
      0
  }

  def printAccountsSummary(idUser: String): Unit = {
    val rs = statement.executeQuery(s"SELECT idAcct, nameA, balance FROM account where iduser = $idUser")
    while(rs.next()){
      val idAcct = rs.getString("idAcct")
      val nameA = rs.getString("nameA")
      val balance = rs.getFloat("balance")
      if(balance >= 0){
        println(String.format("%s : $%.02f : %s", idAcct, balance, nameA))
      }
      else{
        println(String.format("%s : $(%.02f) : %s", idAcct, balance, nameA))
      }
    }
  }

  def updateBalance(idAcct : String)  = {
    var balance : Float = 0
    val rs = statement.executeQuery(s"SELECT amount FROM transact WHERE idAcct = $idAcct")
    while(rs.next()){
      val amount = rs.getFloat("amount")
      balance += amount
    }
    val stmt : PreparedStatement = connection.prepareStatement("UPDATE account SET balance = ? WHERE idAcct = ?")
    stmt.setFloat(1, balance)
    stmt.setString(2, idAcct)
    stmt.executeUpdate()
  }

  def numUserAccounts(idUser : String) : Int = {
    val rs = statement.executeQuery(s"SELECT COUNT(*) FROM account WHERE iduser = $idUser")
    if(rs.next())
      rs.getInt(1)
    else
      0
  }

  def validateAccount(id : String) : Boolean = {
    val rs = statement.executeQuery(s"SELECT nameA FROM account WHERE idAcct = $id")
    if(rs.next())
      true
    else
      false
  }

  def printAcctTransHistory(id : String) = {
    val rs = statement.executeQuery(s"SELECT datet, amount, memo FROM transact WHERE idAcct = $id")
      while(rs.next()){
        val date = rs.getString("datet")
        val amount = rs.getFloat("amount")
        val memo = rs.getString("memo")
        if(amount >= 0){
          println(String.format("%s : $%.02f : %s", date, amount, memo))
        }
        else{
          println(String.format("%s : $(%.02f) : %s", date, amount, memo))
        }
      }
  }

  def addAcctTransaction(idAcct : String, amount : Float, memo : String) = {
    val date : Date = new Date()
    val dateStr : String = date.toString
    val stmt : PreparedStatement = connection.prepareStatement("INSERT INTO transact (amount, memo, datet, idAcct) values (?, ?, ?, ?)")
    stmt.setFloat(1, amount)
    stmt.setString(2, memo)
    stmt.setString(3, dateStr)
    stmt.setString(4, idAcct)
    stmt.executeUpdate()
  }

}
