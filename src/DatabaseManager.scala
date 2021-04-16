import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.Date

class DatabaseManager(var url : String, var username : String, var password : String) {
  private var connection : Connection = _
  //Make the connection to the database.
  try{
    connection = DriverManager.getConnection(url, username, password)
    println("DATABASE SUCCESSFULLY CONNECTED")
  }catch{
    case e: Exception => e.printStackTrace()
  }

  //Prepare the statement for the database queries.
  private val statement = connection.createStatement()

  /**
   * login: Validates the user's access data.
   * @param id  User identification number.
   * @param pin Account password
   * @return True if both data are correct, otherwise false.
   */
  def login(id : String, pin : String) : Boolean = {
    val rs = statement.executeQuery(s"SELECT nameUser, lastName FROM user WHERE iduser = $id AND pin = $pin")
    if(rs.next())
      true
    else
      false
  }

  /**
   * getBankName: Check the name of the current bank.
   * @param id    Bank id to consult.
   * @return      Name of the bank.
   */
  def getBankName(id : Int) : String = {
    val rs = statement.executeQuery(s"SELECT nameB FROM bank WHERE idBank = $id")
    if(rs.next())
      rs.getString("nameB")
    else
      null
  }

  /**
   * getUserName: Check the name of the current user.
   * @param id    User id to consult.
   * @return      Name of the user.
   */
  def getUserName(id : String) : String = {
    val rs = statement.executeQuery(s"SELECT nameUser FROM user WHERE iduser = $id")
    if(rs.next()) {
      rs.getString("nameUser")
    } else
        null
  }

  /**
   * getAcctBalance: Check the balance of the current account.
   * @param id    Account id to consult.
   * @return      Account balance.
   */
  def getAcctBalance(id : String) : Float ={
    val rs = statement.executeQuery(s"SELECT balance FROM account WHERE idAcct = $id")
    if(rs.next()){
      rs.getFloat("balance")
    }
    else
      0
  }

  /**
   * printAccountsSummary: Print the summary of user accounts.
   * @param idUser         User id for the query.
   */
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

  /**
   * updateBalance: Adjusts an account balance based on recent transactions.
   * @param idAcct  Account id to consult.
   */
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

  /**
   * numUserAccounts: Counts the number of accounts of a user.
   * @param idUser    User id for the query.
   * @return          Number of the user accounts.
   */
  def numUserAccounts(idUser : String) : Int = {
    val rs = statement.executeQuery(s"SELECT COUNT(*) FROM account WHERE iduser = $idUser")
    if(rs.next())
      rs.getInt(1)
    else
      0
  }

  /**
   * validateAccount: Validates the existence of the account number entered by the user.
   * @param id        Id of the account to validate.
   * @return          True if data is correct, otherwise false.
   */
  def validateAccount(id : String) : Boolean = {
    val rs = statement.executeQuery(s"SELECT nameA FROM account WHERE idAcct = $id")
    if(rs.next())
      true
    else
      false
  }

  /**
   * printAcctTransHistory: Print the transaction history of an account.
   * @param id              Account id to consult.
   */
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

  /**
   * addAcctTransaction: Add a transaction to an account.
   * @param idAcct       Id of the account to which the transaction will be added.
   * @param amount       Transaction amount.
   * @param memo         Identification message for the transaction.
   */
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
