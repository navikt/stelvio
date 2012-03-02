from ApplicationManagement import remove_v


class TestApplicationManagement(unittest.TestCase):
	def test_remove_v(self):
		before = "nav-tjeneste-oppgave_v1"
		desiredResult = "nav-tjeneste-oppgave"
		self.assertEqual(before, desiredResult)
		
unittest.main()