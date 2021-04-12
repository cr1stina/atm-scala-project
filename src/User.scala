import java.util.ArrayList
import java.security.{MessageDigest, NoSuchAlgorithmException}

/**
 * Class User
 * @param name      user name.
 * @param lastName  user last name.
 * @param pin       user's pin.
 * @param bank      bank to which the user belongs.
 */

class User(var name : String, var lastName : String, var pin : String, var bank : Bank){
  /**
   * id: user's id.
   * pinHash: variable to encrypt the pin.
   * accounts: list of accounts owned by the user.
   * md: cryptographic hash function.
   */
  private var id : String = _
  private var pinHash : Array[Byte] = _
  private val accounts : ArrayList[Account] = new ArrayList[Account]()
  private val md : MessageDigest = MessageDigest.getInstance("MD5")

  //initialize pinHash and id
  pinHash = md.digest(pin.getBytes())
  id = bank.getNewUserID()

  //add a new account to the user
  def addAccount(account: Account) = {
    this.accounts.add(account)
  }

  //returns the user's id
  def get_id(): String = {
    id
  }

  /**
   * validatePin: verify that the user's pin is correct.
   * @param pin pin's user.
   * @return    true, if the pin is correct otherwise false
   */
  def validatePin(pin : String) : Boolean = {
    val md: MessageDigest = MessageDigest.getInstance("MD5")
    MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash)
  }

  //returns the user name
  def get_name() : String ={
    name
  }

  //prints the accounts summary
  def printAccountsSummary() = {
    println("\n" + this.name + "'s account summary")
      for(i <- 0 until this.accounts.size()){
          println((i + 1) + ") " + this.accounts.get(i).getSummary())
      }
    println()
  }

  //returns the size of the accounts list
  def numAccounts(): Int = {
    this.accounts.size()
  }

  //prints the transaction history
  def printAcctTransHistory(acc: Int) = {
      this.accounts.get(acc).printTransHistory()
  }

  /**
   * getAcctBalance:
   * @param acc   account index to search.
   * @return      balance of the current account.
   */
  def getAcctBalance(acc: Int): Double = {
    this.accounts.get(acc).get_balance()
  }

  /**
   * getAcctId:
   * @param acc   account index to search.
   * @return      id of the current account.
   */
  def getAcctId(acc: Int): String = {
    this.accounts.get(acc).get_id()
  }

  /**
   * addAcctTransaction:
   * @param acc     account index.
   * @param amount  transaction amount.
   * @param memo    identification message
   */
  def addAcctTransaction(acc : Int, amount : Double, memo : String) = {
    this.accounts.get(acc).addTransaction(amount, memo)
  }

};
