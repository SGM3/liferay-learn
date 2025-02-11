import com.liferay.headless.admin.taxonomy.client.dto.v1_0.TaxonomyCategory;
import com.liferay.headless.admin.taxonomy.client.resource.v1_0.TaxonomyCategoryResource;

public class TaxonomyCategory_PUT_ById {

	/**
	 * java -classpath .:* -DtaxonomyCategoryId=1234 TaxonomyCategory_PUT_ById
	 */
	public static void main(String[] args) throws Exception {
		TaxonomyCategoryResource.Builder builder =
			TaxonomyCategoryResource.builder();

		TaxonomyCategoryResource taxonomyCategoryResource =
			builder.authentication(
				"test@liferay.com", "test"
			).build();

		TaxonomyCategory taxonomyCategory =
			taxonomyCategoryResource.putTaxonomyCategory(
				String.valueOf(System.getProperty("taxonomyCategoryId")),
				new TaxonomyCategory() {
					{
						description = "Goo";
						name = "Baker";
					}
				});

		System.out.println(taxonomyCategory);
	}

}