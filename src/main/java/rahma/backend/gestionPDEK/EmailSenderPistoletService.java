package rahma.backend.gestionPDEK;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

@Service
public class EmailSenderPistoletService {

    private final JavaMailSender mailSender;

    
    public EmailSenderPistoletService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    /************************************ Erreur *********************************************************************/

    @Async
    public void sendEmailErreurToTechniciens(String toEmail ,String nomResponsable ,String localisation, 
    	    String numPistolet,String couleurPistolet,String categoriePistolet,  
    		String valeurMesuree, String limitesAcceptables) {
        try {
            String subject = "[URGENT] Dépasse des limites de contrôle - Zone Rouge détectée";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("pdekgestion@gmail.com", "Système Alerte Qualité PDEK");
            
            String htmlContent = buildEmailErreurContent(nomResponsable,localisation , numPistolet , couleurPistolet
            		, categoriePistolet,  valeurMesuree, limitesAcceptables);
            helper.setText(htmlContent, true);
            
            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", imageResource);
            
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Échec d'envoi de l'email à technicien ", e);
        }
    }

    private String buildEmailErreurContent(String nomResponsable ,String localisation
    	 , String numPistolet,String couleurPistolet,String categoriePistolet,  
    		String valeurMesuree, String limitesAcceptables) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; }
                    .header { padding: 15px; text-align: center; border-bottom: 2px solid #d9534f; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .alert-title { color: #d9534f; font-size: 24px; font-weight: bold; margin: 10px 0; }
                    .alert-box { border: 1px solid #d9534f; border-left: 4px solid #d9534f; padding: 10px 15px; margin: 15px 0; }
                    .details { margin: 15px 0; }
                    .detail-item { margin-bottom: 8px; }
                    .actions { margin-top: 20px; }
                    .footer { margin-top: 20px; padding-top: 10px; border-top: 1px solid #eee; font-size: 12px; color: #777; text-align: center; }
                    .logo { max-width: 200px; height: auto; }
                    .value-critical { color: #d9534f; font-weight: bold; }
                </style>
            </head>
		    <body>
			  <div class="header">
			    <img src="cid:logoImage" alt="Logo" class="logo">
			    <div class="alert-title">ALERTE QUALITÉ - ZONE ROUGE</div>
			  </div>
			
			  <div class="content">
               <p>Bonjour <strong>%s</strong>, Technicien Qualité en charge du process "Montage Pistolet" dans la plant %s,</p>
			
			    <div class="alert-box">
			      <p style="font-weight: bold; color: #d9534f; margin: 0;">
			        Une valeur hors limites de contrôle a été détectée (Zone Rouge).
			      </p>
			    </div>
			
			    <h3 style="color: #d9534f;">Détails de la non-conformité :</h3>
			    <div class="details">
			      <div class="detail-item"><strong>Numéro de pistole  :</strong> %s </div>
			      <div class="detail-item"><strong>Couleur de pistolet  :</strong> %s </div>
			      <div class="detail-item"><strong>Catégorie de pistolet  :</strong> %s </div>
			      <div class="detail-item"><strong>Valeur mesurée :</strong> <span class="value-critical">%s </span></div>
			      <div class="detail-item"><strong>Limites acceptables :</strong> %s </div>
			      <div class="detail-item"><strong>Description :</strong> Écart important détecté lors du remplie pdek de pistolet .</div>
			      <div class="detail-item"><strong>Date/Heure :</strong> %s</div>
			     </div>
			
			    <h3 style="color: #d9534f;">Actions requises :</h3>
			    <ol class="actions">
			      <li>Arrêt immédiat du processus concerné</li>
			      <li>Contrôle rétroactif des produits depuis le dernier contrôle valide</li>
			      <li>Analyse des causes racines (5M)</li>
			      <li>Rapport d'analyse dans les 24 heures</li>
			    </ol>
			
			    <p>Veuillez confirmer la prise en charge de cette alerte par retour d'email.</p>
			  </div>
			
			  <div class="footer">
			    <p>Ce message a été généré automatiquement par le système de monitoring qualité.</p>
			    <p>© 2025 PDEK System | Support: support@pdek.com</p>
			  </div>
			</body>

            </html>
            """, 
          nomResponsable ,localisation ,  numPistolet ,couleurPistolet,categoriePistolet, 
          valeurMesuree, limitesAcceptables,
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy        HH:mm")),
            Year.now().toString());
    }
    /************************************************* Warning *************************************************/
    @Async
    public void sendEmailWarningToTechniciens(String toEmail, String nomResponsable ,String localisation
    		, String numPistolet , String couleurPistolet,String categoriePistolet, 
    		String valeurMesuree, String limitesAcceptables) {
        try {
            String subject = "[URGENT] Dépasse les limites d'alarme - Zone Jaune détectée";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("pdekgestion@gmail.com", "Système Alerte Qualité PDEK");
            
            String htmlContent = buildEmailWarningContent(nomResponsable,localisation, numPistolet , categoriePistolet, 
            		couleurPistolet,  valeurMesuree, limitesAcceptables);
            helper.setText(htmlContent, true);
            
            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", imageResource);
            
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Échec d'envoi de l'email à l'agent qualité", e);
        }
    }

    private String buildEmailWarningContent(String nomResponsable ,String localisation
    		, String numPistolet ,String couleurPistolet,String categoriePistolet,  
    		String valeurMesuree, String limitesAcceptables) {
    	   return String.format("""
    	            <!DOCTYPE html>
    	            <html>
    	            <head>
    	                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    	                <style>
    	                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: auto; }
    	                    .header { padding: 15px; text-align: center; border-bottom: 2px solid #8ea2c6; }
    	                    .content { padding: 20px; background-color: #f9f9f9; }
    	                    .warning-title { color: #8ea2c6; font-size: 24px; font-weight: bold; margin: 10px 0; }
    	                    .warning-box { border: 1px solid #8ea2c6; border-left: 4px solid #8ea2c6; padding: 10px 15px; margin: 15px 0; background-color: #fffae6; }
    	                    .details { margin: 15px 0; }
    	                    .detail-item { margin-bottom: 8px; }
    	                    .actions { margin-top: 20px; }
    	                    .footer { margin-top: 20px; padding-top: 10px; border-top: 1px solid #eee; font-size: 12px; color: #777; text-align: center; }
    	                    .logo { max-width: 200px; height: auto; }
    	                    .value-warning { color: red; font-weight: bold; }
    	                </style>
    	            </head>
    	            <body>
    	                <div class="header">
    	                    <img src="cid:logoImage" alt="Logo" class="logo">
    	                    <div class="warning-title">ALERTE QUALITÉ - ZONE JAUNE</div>
    	                </div>
    	                
    	                <div class="content">
               <p>Bonjour <strong>%s</strong>, Technicien Qualité en charge du process "Montage Pistolet" dans la plant %s,</p>
    	                    
    	                    <div class="warning-box">
    	                        <p style="font-weight: bold; color: #003d89; margin: 0;">
    	                           ⚠ Une valeur hors limites d'alarme a été détectée  (Zone jaune).
    	                        </p>
    	                    </div>
    	                    
    	                   <h3 style="color: #d9534f;">Détails du non-conformité :</h3>
    	                    <div class="details">
			    	          <div class="detail-item"><strong>Numéro de pistole  :</strong> %s </div>
						      <div class="detail-item"><strong>Couleur de pistolet  :</strong> %s </div>
						      <div class="detail-item"><strong>Catégorie de pistolet  :</strong> %s </div>
						      <div class="detail-item"><strong>Valeur mesurée :</strong> <span class="value-critical">%s </span></div>
						      <div class="detail-item"><strong>Limites acceptables :</strong> %s </div>
						      <div class="detail-item"><strong>Description :</strong> Écart important détecté lors du remplie pdek de pistolet .</div>
						      <div class="detail-item"><strong>Date/Heure :</strong> %s</div>						
    	                    </div>
    	                    
    	                    <h3 style="color: #003d89;">Actions recommandées :</h3>
    	                    <ol class="actions">
    	                        <li>Vérification immédiate du processus concerné</li>
    	                        <li>Contrôle des paramètres de production</li>
    	                        <li>Analyse préventive des causes potentielles</li>
    	                        <li>Rapport d'évaluation dans les 24 heures</li>
    	                    </ol>
    	                    
    	                    <p>Veuillez confirmer la prise en compte de cette alerte par retour d'email.</p>
    	                </div>
    	                
    	                <div class="footer">
    	                    <p>Ce message a été généré automatiquement par le système de monitoring qualité.</p>
    	                    <p>© %s PDEK System | Support: support@pdek.com</p>
    	                </div>
    	            </body>
    	            </html>
    	            """, 
    	            nomResponsable ,localisation ,  numPistolet ,couleurPistolet,categoriePistolet, 
    	            valeurMesuree, limitesAcceptables,
    	            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy        HH:mm")),
    	              Year.now().toString());
    	    }
            }                