public class QueryBuilder 
{
    String query = "SELECT Fname, Lname, Pname, Dname FROM ((EMPLOYEE JOIN WORKS_ON ON Ssn = Essn) JOIN PROJECT ON Pno = Pnumber) JOIN DEPARTMENT ON Dno = Dnum"; //base query gives all names with repeats if the employee works on multible projects

    /**
     * Constructor method for a SQL database using the same Schema as COMPANY on our class server
     * @param departmentNot this value should be true if the box is checked to apply the not operator
     * @param departmentArray all of the departments as strings for the query
     * @param projectNot this value should be true if the box is checked to apply the not operator
     * @param projectArray all of the projects as strings for the query
     */
    public QueryBuilder(boolean departmentNot, String[] departmentArray, boolean projectNot, String[] projectArray)
    {
       if (departmentArray != null && projectArray != null) // if we have a list for both department and array
       {    
            query += " WHERE ";
            if(departmentNot && projectNot)// if both not boxes are checked
            {
                for(int i = 0; i < departmentArray.length; i++)
                {   
                    if(i < (departmentArray.length - 1))
                        query += "Dname <> \"" + departmentArray[i] + "\" OR ";
                    else
                        query += "Dname <> \"" + departmentArray[i] + "\"";
                }

                query += " AND ";

                for(int i = 0; i < projectArray.length; i++)
                {
                    if(i < (projectArray.length - 1))
                        query += "Pname <> \"" + projectArray[i] + "\" OR ";
                    else
                        query += "Pname <> \"" + projectArray[i] + "\"";
                }
            }
            else if(departmentNot && !projectNot)// if only the not box for department is checked
            {
                for(int i = 0; i < departmentArray.length; i++)
                {   
                    if(i < (departmentArray.length - 1))
                        query += "Dname <> \"" + departmentArray[i] + "\" OR ";
                    else
                        query += "Dname <> \"" + departmentArray[i] + "\"";
                }

                query += " AND ";

                for(int i = 0; i < projectArray.length; i++)
                {
                    if(i < (projectArray.length - 1))
                        query += "Pname = \"" + projectArray[i] + "\" AND ";
                    else
                        query += "Pname = \"" + projectArray[i] + "\"";
                }

            }
            else if(!departmentNot && projectNot)// if only the not box for project is checked
            {
                for(int i = 0; i < departmentArray.length; i++)
                {   
                    if(i < (departmentArray.length - 1))
                        query += "Dname = \"" + departmentArray[i] + "\" AND ";
                    else
                        query += "Dname = \"" + departmentArray[i] + "\"";
                }

                query += " AND ";

                for(int i = 0; i < projectArray.length; i++)
                {
                    if(i < (projectArray.length - 1))
                        query += "Pname <> \"" + projectArray[i] + "\" OR ";
                    else
                        query += "Pname <> \"" + projectArray[i] + "\"";
                }
            }
            else // if both not boxes are not checked
            {
                for(int i = 0; i < departmentArray.length; i++)
                {   
                    if(i < (departmentArray.length - 1))
                        query += "Dname = \"" + departmentArray[i] + "\" AND ";
                    else
                        query += "Dname = \"" + departmentArray[i] + "\"";
                }

                query += " AND ";

                for(int i = 0; i < projectArray.length; i++)
                {
                    if(i < (projectArray.length - 1))
                        query += "Pname = \"" + projectArray[i] + "\" AND ";
                    else
                        query += "Pname = \"" + projectArray[i] + "\"";
                }
            }
       }
       else if (departmentArray != null && projectArray == null) // if we have only a list for department
       {
            query += " WHERE ";
            if (departmentNot) // if the not box is checked for department
            {
                for(int i = 0; i < departmentArray.length; i++)
                {   
                    if(i < (departmentArray.length - 1))
                        query += "Dname <> \"" + departmentArray[i] + "\" OR ";
                    else
                        query += "Dname <> \"" + departmentArray[i] + "\"";
                }

            }
            else// if the not box is not checked for department
            {
                for(int i = 0; i < departmentArray.length; i++)
                {   
                    if(i < (departmentArray.length - 1))
                        query += "Dname = \"" + departmentArray[i] + "\" AND ";
                    else
                        query += "Dname = \"" + departmentArray[i] + "\"";
                }
            }
       }
       else if (departmentArray == null && projectArray != null) // if we have only a list for project
       {
            query += " WHERE ";
            if (projectNot)//if the not box is checked for project
            {
                for(int i = 0; i < projectArray.length; i++)
                {
                    if(i < (projectArray.length - 1))
                        query += "Pname <> \"" + projectArray[i] + "\" OR ";
                    else
                        query += "Pname <> \"" + projectArray[i] + "\"";
                }

            }
            else //if the not box is not checked for project
            {
                for(int i = 0; i < projectArray.length; i++)
                {
                    if(i < (projectArray.length - 1))
                        query += "Pname = \"" + projectArray[i] + "\" AND ";
                    else
                        query += "Pname = \"" + projectArray[i] + "\"";
                }
            }
       }

    }
    /**
     * creates the base query with no filters 
     */
    public QueryBuilder()
    {
        
    }

    public String toString()
    {
        return query + ";"; 
    }
}