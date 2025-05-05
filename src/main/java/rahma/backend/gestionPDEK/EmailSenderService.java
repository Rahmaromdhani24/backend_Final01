package rahma.backend.gestionPDEK;

import org.springframework.beans.factory.annotation.Autowired;
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
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    /************************************ Erreur *********************************************************************/

    @Async
    public void sendEmailErreurToAgentQualite(String toEmail ,String nomResponsable ,String localisation, String nomProcess , String sectionFil ,
    		String posteErreur,  String descriptionErreur, String valeurMesuree, String limitesAcceptables) {
        try {
            String subject = "[URGENT] Dépasse des limites de contrôle - Zone Rouge détectée";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("pdekgestion@gmail.com", "Système Alerte Qualité PDEK");
            
            String htmlContent = buildEmailErreurContent(nomResponsable,localisation ,nomProcess, sectionFil , posteErreur, 
            		descriptionErreur,  valeurMesuree, limitesAcceptables);
            helper.setText(htmlContent, true);
            
            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", imageResource);
            
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Échec d'envoi de l'email à l'agent qualité", e);
        }
    }

    private String buildEmailErreurContent(String nomResponsable,String localisation, String nomProcess,  String sectionFil , String posteErreur, 
            String descriptionErreur,  String valeurMesuree, String limitesAcceptables) {
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
                    <p>Bonjour <strong>%s</strong>,</p>
                    
                    <div class="alert-box">
                        <p style="font-weight: bold; color: #d9534f; margin: 0;">
                           Une valeur hors limites de contrôle a été détectée (Zone Rouge).
                        </p>
                    </div>
                    
                    <h3 style="color: #d9534f;">Détails du non-conformité :</h3>
                    <div class="details">
                        <div class="detail-item"><strong>Localisation :</strong> %s</div>
                        <div class="detail-item"><strong>Process :</strong> %s</div>
                        <div class="detail-item"><strong>Section de Fil :</strong> %s</div>
                        <div class="detail-item"><strong>Poste/Machine :</strong> %s</div>
                        <div class="detail-item"><strong>Valeur mesurée :</strong> <span class="value-critical">%s</span></div>
                        <div class="detail-item"><strong>Limites acceptables :</strong> %s</div>
                        <div class="detail-item"><strong>Description :</strong> %s</div>
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
                    <p>© %s PDEK System | Support: support@pdek.com</p>
                </div>
            </body>
            </html>
            """, 
            nomResponsable, 
            localisation,
            nomProcess ,
            sectionFil ,
            posteErreur, 
            valeurMesuree,
            limitesAcceptables,
            descriptionErreur,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            Year.now().toString());
    }
    /************************************************* Warning *************************************************/
    @Async
    public void sendEmailWarningToAgentQualite(String toEmail, String nomResponsable,  String localisation, String nomProcess,
    		String sectionFil, String posteWarning,  String descriptionWarning,String valeurMesuree, String limitesAcceptables) {
        try {
            String subject = "[URGENT] Dépasse les limites d'alarme - Zone Jaune détectée";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("pdekgestion@gmail.com", "Système Alerte Qualité PDEK");
            
            String htmlContent = buildEmailWarningContent(nomResponsable,localisation , nomProcess, sectionFil ,
            		posteWarning, descriptionWarning, valeurMesuree, limitesAcceptables);
            helper.setText(htmlContent, true);
            
            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", imageResource);
            
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Échec d'envoi de l'email à l'agent qualité", e);
        }
    }

    private String buildEmailWarningContent(String nomResponsable,String localisation, String nomProcess, String sectionFil ,
    		String posteWarning, String descriptionWarning,  String valeurMesuree, String limitesAcceptables) {
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
                    <p>Bonjour <strong>%s</strong>,</p>
                    
                    <div class="warning-box">
                        <p style="font-weight: bold; color: #003d89; margin: 0;">
                           ⚠ Une valeur hors limites d'alarme a été détectée  (Zone jaune).
                        </p>
                    </div>
                    
                   <h3 style="color: #d9534f;">Détails du non-conformité :</h3>
                    <div class="details">
                        <div class="detail-item"><strong>Localisation :</strong> %s</div>
                        <div class="detail-item"><strong>Process :</strong> %s</div>
                        <div class="detail-item"><strong>Section de Fil :</strong> %s</div>
                        <div class="detail-item"><strong>Poste/Machine :</strong> %s</div>
                        <div class="detail-item"><strong>Valeur mesurée :</strong> <span class="value-critical">%s</span></div>
                        <div class="detail-item"><strong>Limites acceptables :</strong> %s</div>
                        <div class="detail-item"><strong>Description :</strong> %s</div>
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
            nomResponsable, 
            localisation,
            nomProcess, 
            sectionFil ,
            posteWarning, 
            valeurMesuree,
            limitesAcceptables,
            descriptionWarning,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            Year.now().toString());
    }
    /******************************** Chef de ligne erreur ***********************************************/
    @Async
    public void sendEmailErreurToChefDeLigne(String toEmail, String nomResponsable,  String localisation, String nomProcess, String sectionFil ,
            String posteErreur, String descriptionErreur, String valeurMesuree, String limitesAcceptables) {
        try {
            String subject = "[URGENT] Dépasse des limites de contrôle - Zone Rouge détectée";
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("pdekgestion@gmail.com", "Système Alerte PDEK");
            
            String htmlContent = buildEmailErreurChefLigneContent(
                nomResponsable, localisation ,nomProcess,sectionFil ,  posteErreur, 
                descriptionErreur, valeurMesuree, limitesAcceptables);
            
            helper.setText(htmlContent, true);
            
            // Configuration de l'image avec taille contrôlée
            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", imageResource, "image/png");
            
            mailSender.send(message);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Échec d'envoi de l'email au chef de ligne", e);
        }
    }

    private String buildEmailErreurChefLigneContent(String nomResponsable, String localisation , String nomProcess,
            String sectionFil , String posteErreur, String description, 
            String valeurMesuree, String limitesAcceptables) {
        
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String currentYear = Year.now().toString();

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.5;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 10px;
                    }
                    .header {
                        text-align: center;
                        margin-bottom: 20px;
                    }
                    .logo {
                        height: 50px;
                        width: auto;
                    }
                    .alert-title {
                        color: #d9534f;
                        font-size: 20px;
                        font-weight: bold;
                        margin: 10px 0;
                    }
                    .alert-box {
                        background-color: #f8d7da;
                        border-left: 4px solid #d9534f;
                        padding: 10px 15px;
                        margin: 15px 0;
                    }
                    .detail-item {
                        margin-bottom: 8px;
                    }
                    .detail-label {
                        font-weight: bold;
                        display: inline-block;
                        width: 150px;
                    }
                    .critical-value {
                        color: #d9534f;
                        font-weight: bold;
                    }
                    .footer {
                        margin-top: 20px;
                        font-size: 12px;
                        color: #777;
                        text-align: center;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <img src="cid:logoImage" alt="Logo" class="logo">
                    <div class="alert-title">ALERTE PRODUCTION - ZONE ROUGE</div>
                </div>
                
                <p>Chef de Ligne <strong>%s</strong>,</p>
                
                <div class="alert-box">
                    <p style="margin: 0; font-weight: bold;">Une valeur hors limites de contrôle a été détectée (Zone Rouge).</p>
                </div>
                
                <h3 style="color: #d9534f;">Détails du problème :</h3>
                <div class="detail-item"><span class="detail-label">Localisation :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Process :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Section de Fil :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Poste/Machine :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Valeur mesurée :</span> <span class="critical-value">%s</span></div>
                <div class="detail-item"><span class="detail-label">Limites acceptables :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Description :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Date/Heure :</span> %s</div>
                <div class="detail-item"><span class="detail-label">Temps d'arrêt :</span> À évaluer</div>
                
                <h3 style="color: #d9534f; margin-top: 20px;">Actions requises :</h3>
                <ol>
                    <li>Arrêt immédiat du processus concerné</li>
                    <li>Contrôle rétroactif des produits</li>
                    <li>Analyse des causes racines</li>
                    <li>Coordination avec le service qualité</li>
                    <li>Rapport dans les 24 heures</li>
                </ol>
                
                <div class="footer">
                    <p>Ce message a été généré automatiquement</p>
                    <p>© %s PDEK System | Support: support@pdek.com</p>
                </div>
            </body>
            </html>
            """.formatted(
                nomResponsable,
                localisation,
                nomProcess,
                sectionFil ,
                posteErreur,
                valeurMesuree,
                limitesAcceptables,
                description,
                currentDateTime,
                currentYear
            );
    }
			
			    /*********************************** Chef de ligne email de warning *****************/
			    @Async
			    public void sendEmailWarningToChefDeLigne(String toEmail, String nomResponsable, String localisation ,String nomProcess, 
			    		String sectionFil ,String posteWarning, String descriptionWarning,  String valeurMesuree, String limitesAcceptables) {
			        try {
			            String subject = "[URGENT] Dépasse les limites d'alarme - Zone Jaune détectée";
			            
			            MimeMessage message = mailSender.createMimeMessage();
			            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			            
			            helper.setTo(toEmail);
			            helper.setSubject(subject);
			            helper.setFrom("pdekgestion@gmail.com", "Système Alerte Qualité PDEK");
			            
			            String htmlContent = buildEmailWarningContentChefLigne(nomResponsable,localisation , nomProcess,sectionFil ,
			            		posteWarning, descriptionWarning,  valeurMesuree, limitesAcceptables);
			            helper.setText(htmlContent, true);
			            
			            ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
			            helper.addInline("logoImage", imageResource);
			            
			            mailSender.send(message);
			            
			        } catch (MessagingException | UnsupportedEncodingException e) {
			            throw new RuntimeException("Échec d'envoi de l'email à l'agent qualité", e);
			        }
			    }
			    private String buildEmailWarningContentChefLigne(String nomResponsable, String nomProcess,String localisation, 
			    		String sectionFil , String posteWarning,  String descriptionWarning,  String valeurMesuree, String limitesAcceptables) {
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
			                    <p>Bonjour <strong>%s</strong>,</p>
			                    
			                    <div class="warning-box">
			                        <p style="font-weight: bold; color: #003d89; margin: 0;">
			                           ⚠ Une valeur hors limites d'alarme a été détectée  (Zone jaune).
			                        </p>
			                    </div>
			                    
			                    <h3 style="color: #003d89;">Détails de l'alerte :</h3>
			                    <div class="details">
			                       <div class="detail-item"><strong>Localisation :</strong> %s</div>
			                        <div class="detail-item"><strong>Process :</strong> %s</div>
			                        <div class="detail-item"><strong>Section de Fil :</strong> %s</div>
			                        <div class="detail-item"><strong>Poste/Machine :</strong> %s</div>
			                        <div class="detail-item"><strong>Valeur mesurée :</strong> <span class="value-warning">%s</span></div>
			                        <div class="detail-item"><strong>Limites acceptables :</strong> %s</div>
			                        <div class="detail-item"><strong>Description :</strong> %s</div>
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
			            nomResponsable, 
			            localisation ,
			            nomProcess, 
			            sectionFil ,
			            posteWarning, 
			            valeurMesuree,
			            limitesAcceptables,
			            descriptionWarning,
			            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
			            Year.now().toString());
			    }
                /************************************************************ */
                @Async
                public void sendEmailValidationToAgentQualite(String toEmail, String nomResponsable, String localisation, String nomProcess,
                        String sectionFil, String posteMachine, String descriptionPDEK, String dateRemplissage, String heureRemplissage) {
                    try {
                        String subject = "[ACTION REQUISE] Validation du PDEK rempli";
                        
                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                        
                        helper.setTo(toEmail);
                        helper.setSubject(subject);
                        helper.setFrom("pdekgestion@gmail.com", "Système Alerte Qualité PDEK");
                        
                        String htmlContent = buildEmailValidationContent(nomResponsable, localisation, nomProcess, sectionFil, 
                                descriptionPDEK, dateRemplissage, heureRemplissage);
                        helper.setText(htmlContent, true);
                        
                        ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
                        helper.addInline("logoImage", imageResource);
                        
                        mailSender.send(message);
                        
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException("Échec d'envoi de l'email à l'agent qualité", e);
                    }
                }
                
                private String buildEmailValidationContent(String nomResponsable, String localisation, String nomProcess, String sectionFil,
                        String descriptionPDEK, String dateRemplissage, String heureRemplissage) {
                    return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                            <style>
                                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 500px; margin: auto; padding: 20px; }
                                .header { text-align: center; }
                                .title { color: #8ea2c6; font-size: 22px; font-weight: bold; margin: 10px 0; }
                                .content { padding: 10px; background-color: #f9f9f9; text-align: center; }
                                .details { margin-top: 20px; text-align: left; }
                                .detail-item { margin-bottom: 6px; }
                                .footer { margin-top: 20px; text-align: center; font-size: 12px; color: #777; }
                                .logo { max-width: 150px; height: auto; }
                            </style>
                        </head>
                        <body>
                            <div class="header">
                                <img src="cid:logoImage" alt="Logo" class="logo">
                                <div class="title">Validation requise pour le PDEK rempli</div>
                            </div>
                            
                            <div class="content">
                                <p>Bonjour <strong>%s</strong>,</p>
                                
                                <p>Un PDEK a été rempli et nécessite votre validation. Veuillez trouver ci-dessous les informations détaillées :</p>
                                
                                <div class="details">
                                    <div class="detail-item"><strong>Localisation :</strong> %s</div>
                                    <div class="detail-item"><strong>Process :</strong> %s</div>
                                    <div class="detail-item"><strong>Section de Fil :</strong> %s</div>
                                    <div class="detail-item"><strong>Description du PDEK :</strong> %s</div>
                                    <div class="detail-item"><strong>Date de remplissage :</strong> %s</div>
                                    <div class="detail-item"><strong>Heure de remplissage :</strong> %s</div>
                                </div>
                                
                                <p>Merci de valider ce PDEK dans les plus brefs délais.</p>
                            </div>
                            
                            <div class="footer">
                                <p>Ce message a été généré automatiquement par le système de gestion PDEK.</p>
                                <p>© %s PDEK System | Support: support@pdek.com</p>
                            </div>
                        </body>
                        </html>
                        """, 
                        nomResponsable, 
                        localisation,
                        nomProcess, 
                        sectionFil,
                        descriptionPDEK,
                        dateRemplissage,
                        heureRemplissage,
                        Year.now().toString());
                }
            }                