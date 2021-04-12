import java.util
import java.util.ArrayList
import scala.util.control.Breaks.break

/**
 * Class Bank:
 * @param name  name of the bank.
 */

class Bank(var name : String) {
  /**
   * users: list of users registered in the bank.
   * accounts: list of the accounts registered in the bank.
   */
  private val users : ArrayList[User] = new ArrayList[User]()
  private val accounts : ArrayList[Account] = new ArrayList[Account]()

  //creates and returns a new id for a new user
  def getNewUserID(): String = {
    var userId : String = null
    val rng = new util.Random()
    val len : Int = 7
    var nonUnique : Boolean = false
    var user : User = null

    do{
        userId = ""
        for(i <- 0 until len){
          userId += rng.nextInt(10).toString()
        }
        nonUnique = false
        for(j <- 0 until this.users.size()) {
          user = this.users.get(j)
          if(userId.compareTo(user.get_id()) == 0){
            nonUnique = true
            break
          }
        }
    }while(nonUnique)

    userId
  }

  //creates and return a new id for a new account
  def getNewAccountID(): String = {
    var accId : String = null
    val rng = new util.Random()
    val len : Int = 10
    var nonUnique : Boolean = false
    var acc : Account = null

    do{
      accId = ""
      for(i <- 0 until len){
        accId += rng.nextInt(10).toString()
      }
      nonUnique = false
      for(j <- 0 until this.accounts.size()) {
        acc = this.accounts.get(j)
        if(accId.compareTo(acc.get_id()) == 0){
          nonUnique = true
          break
        }
      }
    }while(nonUnique)

    accId

  }

  //add a new account to the list
  def addAccount(account: Account) = {
    this.accounts.add(account)
  }

  /**
   * newUser: creates a new user.
   * @param name      name of the new user.
   * @param lastName  last name of the new user.
   * @param pin       pin of the new user.
   * @return          newUser
   */
  def newUser(name : String, lastName : String, pin : String) : User = {
    val user : User = new User(name, lastName, pin, this)
    this.users.add(user)
    val acc : Account = new Account("Savings", user, this)
    this.addAccount(acc)
    println("New user: " + name + " " + lastName + " with ID: " + user.get_id() + " " + "SUCCESSFULLY CREATED")
    user
  }

  /**
   * userLogin: verify that the username and the pin are correct to be able to enter.
   * @param userId  user's id
   * @param pin     user's pin
   * @return        loggedUser
   */
  def userLogin(userId : String, pin : String) : User = {
    var user : User = null
    for(i <- 0 until this.users.size()) {
      user = users.get(i)
      if (user.get_id().compareTo(userId) == 0 && user.validatePin(pin)) {
        return user
      }
    }
    null
  }

  //returns the name of the bank
  def get_name(): String ={
    name
  }


};
