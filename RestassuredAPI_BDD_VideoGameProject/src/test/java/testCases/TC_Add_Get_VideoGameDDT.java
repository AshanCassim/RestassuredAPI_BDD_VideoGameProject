package testCases;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import io.restassured.response.Response;




public class TC_Add_Get_VideoGameDDT  
{
	@Test(priority=1,dataProvider="Data")
	public void test_addNewVideoGame(String id,String name,String releaseDate,String reviewScore,String category,String rating)
	{
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("name", name);
		data.put("releaseDate", releaseDate);
		data.put("reviewScore", reviewScore);
		data.put("category", category);
		data.put("rating", rating);
		
		Response res=
			given()
				.contentType("application/json")
				.body(data)
			.when()
				.post("http://localhost:8080/app/videogames")
				
			.then()
				.statusCode(200)
				.log().body()
				.extract().response();
			
		String jsonString=res.asString();
		Assert.assertEquals(jsonString.contains("Record Added Successfully"), true);
		
	}
	
	
	@Test(priority=2,dataProvider="Data")
	public void test_getVideoGame(String id,String name,String releaseDate,String reviewScore,String category,String rating)
	{
		given()
			.pathParam("id",id)
		.when()
			.get("http://localhost:8080/app/videogames/{id}")
		.then()
			.statusCode(200)
			.log().body()
			.body("videoGame.id", equalTo(id))
			.body("videoGame.name", equalTo(name));
			
	}
	
	
		
	@DataProvider(name="Data")	
	public String [][] getData() throws IOException, FilloException
	{
		
		Fillo f = new Fillo();
		
		Connection connection = f.getConnection("TestData/VideoGameAPIData.xlsx");
		
		String strQuery = "Select * from Sheet1";
		
		Recordset recordset=connection.executeQuery(strQuery); // Execute Query and store result in Recordset
    
		String vdata [][] = new String[recordset.getCount()][6];
		
		int i =0;
		
		while(recordset.next())
		{  // Condition till record set has values
			
			
			vdata[i][0] = recordset.getField("id");
			vdata[i][1] = recordset.getField("name");
			vdata[i][2] = recordset.getField("releaseDate");
			vdata[i][3] = recordset.getField("reviewScore");
			vdata[i][4] = recordset.getField("category");
			vdata[i][5] = recordset.getField("rating");
			
			i++;
			
		}	
			
		return vdata;		
	}

			
}

		
		
			
		
	
	
	
	
	


