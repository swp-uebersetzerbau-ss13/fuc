package swp_compiler_ss13.fuc.test;

import org.junit.Test;
import swp_compiler_ss13.common.backend.Backend;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * ALP5 - Tutor: Alexander Steen
 * Author: Jens Fischer, Hinnerk van Bruinehsen
 */
public class BackendProviderTest {

	@Test
	public void testBackendProvider() {
		ServiceLoader serviceLoader = ServiceLoader.load(Backend.class);
		Iterator iterator = serviceLoader.iterator();
		Object module = null;
		while (iterator.hasNext()) {
			module = iterator.next();
			System.out.println(module.getClass().getSimpleName());
		}

		Backend backend = (Backend) module;

	}


}

