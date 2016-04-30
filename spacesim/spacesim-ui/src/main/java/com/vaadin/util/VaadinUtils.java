package com.vaadin.util;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class VaadinUtils {


	// private static final Logger logger =
	// LoggerFactory.getLogger(VaadinUtils.class);

	// @SuppressWarnings("unchecked")
	public static <T extends Component> T findById(HasComponents root, String id, Class<T> returnType) {
		if (id == null || root == null) {
			return null;
		}
		T found = null;
		for (Component c : root) {
			if (id.equals(c.getId()) && c.getClass().equals(returnType)) {
				return returnType.cast(c);
			}
			if (c instanceof HasComponents) {
				found = findById((HasComponents) c, id, returnType);
				if (found != null) {
					return found;
				}
			}
		}
		return found;
	}
}
